//proposed process
package supersql.dataconstructor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import supersql.codegenerator.AttributeItem;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.common.Utils;
import supersql.db.ConnectDB;
import supersql.db.GetFromDB;
import supersql.db.SQLManager;
import supersql.extendclass.ExtList;
import supersql.parser.FromInfo;
import supersql.parser.SSQLparser;

public class DataConstructor {

	private ExtList data_info;

	private ArrayList<SQLQuery> sqlQueries = null;
	private QueryDivider qd;
	private String key = null;
	private Attribute keyAtt = null;
	private int col = -1;
	private long[] exectime = new long[4];
	private final int ISDIVIS = 0;
	private final int MAKESQL = 1;
	private final int EXECSQL = 2;
	private final int MKETREE = 3;
	private boolean flag = true;
	public static String SQL_string; // added by goto 20130306
										// "FROM�ʤ��������к�"

	public DataConstructor(SSQLparser parser) {

		ExtList sep_sch;
		ExtList sep_data_info;

		MakeSQL msql = null;

		// Make schema
		sep_sch = parser.get_TFEschema().makesch();
		System.out.println("Schema: " + sep_sch);

		// Check Optimization Parameters
		if (GlobalEnv.getOptLevel() == 0 || !GlobalEnv.isOptimizable()
				|| SSQLparser.isDbpediaQuery() || SSQLparser.isJsonQuery()) {
			sqlQueries = null;
		} else {
			// Initialize QueryDivider
			long start = System.nanoTime();

			try {
				qd = new QueryDivider(parser);

				if (qd.MakeGraph()) {
					// if graph was made successfully, divide
					sqlQueries = qd.divideQuery();
				}
			} catch (Exception e) {
				;
				// System.out.println( e.getMessage() ); //commented out by goto
				// 20120620
			}

			long end = System.nanoTime();
			exectime[ISDIVIS] = end - start;
		}

		// Make SQL
		if ((sqlQueries == null || sqlQueries.size() < 2)
				&& !SSQLparser.isDbpediaQuery()) {
			// if graph was not made successfully or
			// if graph has only one connected component
			// query cannot be divided
			msql = new MakeSQL(parser);
		}

		sep_data_info = new ExtList();
		if (SSQLparser.isDbpediaQuery()) {
			sep_data_info = schemaToData(parser, sep_sch, sep_data_info);
		} else if (SSQLparser.isJsonQuery()) {
			sep_data_info = schemaToDataFromApi(parser, msql, sep_sch,
					sep_data_info);
		} else {
			sep_data_info = schemaToData(parser, msql, sep_sch, sep_data_info);
		}
		data_info = sep_data_info;

		Log.out("## Result ##");
		Log.out(data_info);
	}

	private ExtList schemaToDataFromApi(SSQLparser parser, MakeSQL msql,
			ExtList sep_sch, ExtList sep_data_info) {
		String[] fromInfos = SSQLparser.get_from_info_st()
				.split("api\\(|,|\\)");
		String url = fromInfos[1];
		url = url.substring(url.indexOf("'") + 1,
				url.indexOf("'", url.indexOf("'") + 1));
		int attno = parser.get_att_info().size();
		String[] array = new String[attno];
		int i = 0;
		for (Object info : parser.get_att_info().values()) {
			String infoText = ((AttributeItem) info).toString();
			array[i] = infoText;
			i++;
		}
		sep_data_info = getDataFromApi(url, array, msql, sep_sch);
		sep_data_info = makeTree(sep_sch, sep_data_info);
		return sep_data_info;
	}

	private ExtList getDataFromApi(String url,
			String[] array, MakeSQL msql, ExtList sep_sch) {
		ExtList<ExtList<String>> data = new ExtList<ExtList<String>>();
		String createSql = "";
		String insertSql = "";
		try {
			ArrayList<String> newArray = new ArrayList<String>();
			String fromLine = "";
			for(int i = 0; i < array.length; i++){
				String tableName = array[i].split("\\.")[0];
				if(!newArray.contains(tableName)){
					newArray.add(tableName);
					fromLine += " " + tableName + ",";
				}
			}
			fromLine = fromLine.substring(0, fromLine.length() - 1);
			for (int i = 0; i < newArray.size(); i++) {
				ArrayList<String> elements = new ArrayList<String>();
				String element = newArray.get(i);
				String itemsUrl = url.replaceAll(":table_name", element);
				String itemsJson = Utils.sendGet(itemsUrl);
				JSONArray items = new JSONArray(itemsJson);
				for (int j = 0; j < items.length(); j++) {
					JSONObject item = items.getJSONObject(j);
					Iterator<String> keyIterator = item.keys();
					if (j == 0) {
						createSql += "CREATE TABLE " + element + "(";
						while (keyIterator.hasNext()) {
							String key = keyIterator.next();
							createSql += key + ",";
						}
						createSql = createSql.substring(0, createSql.length() - 1) + ");\n";
					}
					insertSql += "INSERT INTO " + element + " VALUES " + "(";
					keyIterator = item.keys();
					while(keyIterator.hasNext()){
						String key = keyIterator.next();
						insertSql += "'" + item.get(key).toString() + "',";
					}
					insertSql = insertSql.substring(0, insertSql.length() - 1) + ");\n";
				}
			}

			msql.setFrom(new FromInfo(fromLine));
			
			String sqlString = msql.makeSQL(sep_sch);

			SQLManager manager = new SQLManager("jdbc:sqlite::memory:",
					GlobalEnv.getusername(), "org.sqlite.JDBC", GlobalEnv.getpassword());
			manager.ExecSQL(sqlString, createSql, insertSql);
			data = manager.GetBody();

			return data;
		} catch (Exception e) {
			System.err.println("Could not connect to the Api server");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	private ExtList schemaToData(SSQLparser parser, ExtList sep_sch,
			ExtList sep_data_info) {
		int attno = parser.get_att_info().size();
		String[] array = new String[attno];
		int i = 0;
		for (Object info : parser.get_att_info().values()) {
			String infoText = ((AttributeItem) info).toString();
			array[i] = infoText;
			i++;
		}
		sep_data_info = getDataFromDBPedia(parser.get_where_info()
				.getSparqlWhereQuery(), array);
		sep_data_info = makeTree(sep_sch, sep_data_info);
		return sep_data_info;
	}

	private ExtList schemaToData(SSQLparser parser, MakeSQL msql,
			ExtList sep_sch, ExtList sep_data_info) {

		long start, end;

		if (msql != null) {
			getFromDB(msql, sep_sch, sep_data_info);
			sep_data_info = makeTree(sep_sch, sep_data_info);

		} else {
			getTuples(sep_sch, sep_data_info);
			start = System.nanoTime();
			sep_data_info = MakeTree(qd.getSchema());
			// System.out.println(sep_data_info);
			end = System.nanoTime();

			exectime[MKETREE] = end - start;
		}

		return sep_data_info;

	}

	private ExtList[] getTuples(ExtList sep_sch, ExtList sep_data_info) {

		long start, end;
		start = System.nanoTime();

		ExtList[] table;
		GetFromDB gfd;
		int comp_size;

		comp_size = sqlQueries.size();
		table = new ExtList[comp_size];

		if (GlobalEnv.isMultiThread()) {
			System.out.println("[Enter MultiThread mode]");
			ConnectDB cdb = new ConnectDB(GlobalEnv.geturl(),
					GlobalEnv.getusername(), GlobalEnv.getDriver(),
					GlobalEnv.getpassword());
			System.out.println(GlobalEnv.geturl() + GlobalEnv.getusername()
					+ GlobalEnv.getpassword());

			cdb.setName("CDB1");
			cdb.run();

			gfd = new GetFromDB(cdb);
		}

		else {
			gfd = new GetFromDB();
		}

		long time = 0;

		// changed by goto 20120630
		System.out.println("sqlQueries.size() = " + sqlQueries.size());
		// for (int i = 0; i < sqlQueries.size(); i++)
		for (int i = 0; i < sqlQueries.size() - 1; i++) {
			table[i] = new ExtList();

			long time1 = System.nanoTime();
			String s = sqlQueries.get(i).getString();
			time += (System.nanoTime() - time1);

			gfd.execQuery(s, table[i]);
			sqlQueries.get(i).setResult(table[i]);
		}

		gfd.close();
		end = System.nanoTime();

		exectime[EXECSQL] = end - start - time;
		exectime[MAKESQL] = time;

		Log.out("## DB result ##");

		return table;

	}

	private ExtList getFromDB(MakeSQL msql, ExtList sep_sch,
			ExtList sep_data_info) {

		// MakeSQL
		long start, end;
		start = System.nanoTime();

		SQL_string = msql.makeSQL(sep_sch);

		end = System.nanoTime();
		exectime[MAKESQL] = end - start;
		Log.out("## SQL Query ##");
		Log.out(SQL_string);

		// Connect to DB
		start = System.nanoTime();

		GetFromDB gfd;
		if (GlobalEnv.isMultiThread()) {
			System.out.println("[Enter MultiThread mode]");
			ConnectDB cdb = new ConnectDB(GlobalEnv.geturl(),
					GlobalEnv.getusername(), GlobalEnv.getDriver(),
					GlobalEnv.getpassword());
			System.out.println(GlobalEnv.geturl() + GlobalEnv.getusername()
					+ GlobalEnv.getpassword());

			cdb.setName("CDB1");
			cdb.run();

			gfd = new GetFromDB(cdb);
		}

		else {
			gfd = new GetFromDB();
		}

		gfd.execQuery(SQL_string, sep_data_info);

		gfd.close();

		end = System.nanoTime();
		exectime[EXECSQL] = end - start;

		Log.info("## DB result ##");
		Log.info(sep_data_info);

		return sep_data_info;

	}

	private ExtList makeTree(ExtList sep_sch, ExtList sep_data_info) {

		// MakeTree
		long start, end;
		start = System.nanoTime();

		TreeGenerator tg = new TreeGenerator();

		sep_data_info = tg.makeTree(sep_sch, sep_data_info);

		end = System.nanoTime();

		exectime[MKETREE] = end - start;

		Log.out("## constructed Data ##");
		Log.out(sep_data_info);

		return sep_data_info;
	}

	public ExtList getData() {
		return data_info;
	}

	private ExtList MakeTree(ExtList schema) {
		// added by ria
		Object o;
		ExtList buf = new ExtList();
		for (int i = 0; i < schema.size(); i++) {
			o = schema.get(i);

			if (!(o instanceof ExtList)) {
				if (keyAtt == null) {
					keyAtt = (Attribute) o;
					buf.add(keyAtt.getTuple());
					// System.out.println(buf);
					key = keyAtt.getTuple().toString();
					col = keyAtt.getColumn();
				} else {
					Attribute a = (Attribute) o;
					if (a == keyAtt) {
						buf.add(keyAtt.getTuple());
						// System.out.println(buf);
						key = keyAtt.getTuple().toString();
					} else {
						// add here checking if the keyAtt is a connector
						buf.add(a.getTuple(key, col));
						// System.out.println(buf);
						a.delTuples(key, col);
					}
				}
			} else if (IsLeaf((ExtList) o)) {

				ExtList obj = (ExtList) o;
				ExtList temp = new ExtList();

				Attribute a = (Attribute) obj.get(0);
				temp.addAll((a.getTuples(key, col)));

				if (temp.size() == 0) {
					flag = false;
				} else {
					flag = true;
				}

				buf.add(temp);
				// System.out.println(buf);

				if (keyAtt != null) {
					keyAtt.delTuples(key, col);
				}

			} else {
				if (schema.size() == 1) {
					ExtList temp = new ExtList();
					do {
						ExtList temp2 = MakeTree((ExtList) o);
						if (!temp2.isEmpty()) {
							temp.add(temp2);
							if (keyAtt != null) {
								keyAtt.delTuples(key, col);
							}
						}
					} while ((keyAtt != null) && keyAtt.getSize() != 0);

					buf.add(temp);
					// System.out.println(buf);
					flag = true;
				} else {
					ExtList temp = new ExtList();
					temp.add(MakeTree((ExtList) o));

					if (flag) {
						buf.add(temp);
						// System.out.println(buf);
					}
				}
			}
		}
		if (!flag) {
			buf = new ExtList();
		}

		return buf;
	}

	private boolean IsLeaf(ExtList sch) {
		for (int i = 0; i < sch.size(); i++) {
			if (sch.get(i) instanceof ExtList)
				return false;
		}
		return true;
	}

	public static ExtList getDataFromDBPedia(String sparqlWhereQuery,
			String[] varNames) {
		BufferedReader br = null;
		String everything = "";
		try {
			br = new BufferedReader(new FileReader("dbpedia.config"));
		} catch (FileNotFoundException e1) {
			System.err.println("*** DBPedia config file not found ***");
			e1.printStackTrace();
			throw new IllegalStateException();
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			everything = sb.toString();
		} catch (IOException e) {
			System.err
					.println("*** Error while reading the Dbpedia config file ***");
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.err
						.println("*** Error while closig the dbpedia config file ***");
				e.printStackTrace();
			}
		}
		try {
			Document doc;
			ExtList data = new ExtList();
			String query = everything + "\nSELECT ";
			for (int i = (varNames.length - 1); i >= 0; i--) {
				query += "?" + varNames[i] + " ";
			}
			query += " WHERE " + sparqlWhereQuery + "";
			doc = Jsoup.connect("http://dbpedia.org/sparql?")
					.data("default-graph-uri", "http://dbpedia.org")
					.data("query", query).data("format", "text/html")
					.data("debug", "on").timeout(0).get();
			Elements tdInfos = doc.getElementsByTag("td");
			int columnCount = 0;
			int rowCount = -1;
			for (Element info : tdInfos) {
				String infoText = info.html();
				columnCount %= varNames.length;
				if (columnCount == 0) {
					ExtList e = new ExtList();
					e.add(infoText);
					data.add(e);
					columnCount += 1;
					rowCount += 1;
				} else {
					((ExtList) data.get(rowCount)).add(infoText);
					columnCount += 1;
				}

			}
			return data;
		} catch (IOException e) {
			System.err
					.println("*** Error while querying dbpedia, please check your internet connection and your query syntax ***");
			throw new IllegalStateException();
		}
	}
}
