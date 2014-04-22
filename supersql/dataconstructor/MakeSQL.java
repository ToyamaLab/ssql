package supersql.dataconstructor;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import supersql.codegenerator.AttributeItem;
import supersql.codegenerator.XML.XMLFunction;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.parser.FromInfo;
import supersql.parser.FromParse;
import supersql.parser.Preprocessor;
import supersql.parser.SSQLparser;
import supersql.parser.WhereInfo;
import supersql.parser.WhereParse;
//ryuryu

public class MakeSQL {

	//test
	private FromInfo from;

	private WhereInfo where;

	private Hashtable atts;

	private ExtList table_group;

	public MakeSQL(SSQLparser p) {
		setFrom(p.get_from_info());
		where = p.get_where_info();
		atts = p.get_att_info();

		MakeGroup mg = new MakeGroup(atts, where);
		table_group = mg.getTblGroup();
		Log.out("[MakeSQL:table_group]" + table_group);

	}

	public String makeSQL(ExtList sep_sch) {

		boolean flag;
		int i, idx;
		Integer itemno;
		ExtList schf;
		Log.out("[sep_sch]=" + sep_sch);
		schf = sep_sch.unnest();
		Log.out("[schf]" + schf);
		//			StringBuffer buf = new StringBuffer("SELECT ");

		//hanki start
		StringBuffer buf;

		if (Preprocessor.isAggregate()) {
			buf = new StringBuffer("SELECT ALL ");
		} else {
			if(SSQLparser.isDistinct()){
				buf = new StringBuffer("SELECT DISTINCT ");
			}
			else{
				buf = new StringBuffer("SELECT ");
			}
			//ryuryu(end)/////////////////////////////////////////////////////
		}
		//hanki end

		int tmp_flag = 0; //ryuryu

		HashSet tg1 = new HashSet();
		for (idx = 0; idx < schf.size(); idx++) {
			itemno = (Integer) (schf.get(idx));
			AttributeItem att1 = (AttributeItem) (atts.get(itemno));

			//ryuryu
			/*if (idx != 0) {
				buf.append(", " + att1.getSQLimage());
			} else {
				buf.append(att1.getSQLimage());
			}*/

			//ryuryu(start)//////////////////////////////////////////////////////////////////////////////////////////
			if(SSQLparser.xpathExist == 0){
				if (idx != 0) {
					buf.append(", " + att1.getSQLimage());
				}
				else{
					buf.append(att1.getSQLimage());
				}
			}


			else if(SSQLparser.xpathExist == 1){

				if (idx != 0) {
					if(att1.getSQLimage().equals(SSQLparser.tmpXpath1)){
						buf.append(", " + SSQLparser.Xpath.replace("\"", "'"));
						tmp_flag = 1;
					}

					else if(att1.getSQLimage().equals(SSQLparser.tmpXmlQuery1)){
						String tmp_xmlquery = new String();
						tmp_xmlquery = (SSQLparser.DB2_XQUERY.replace("\"", "'")).replace((SSQLparser.tmpXmlQuery2 + "',"), (SSQLparser.tmpXmlQuery2 + "' PASSING "));
						tmp_xmlquery = tmp_xmlquery.replace((SSQLparser.tmpXmlQuery1 + ")"), (SSQLparser.tmpXmlQuery1 + " AS \"a\")"));
						buf.append(", " + tmp_xmlquery);
						tmp_flag = 1;
					}

					else{
						if(tmp_flag==0){
							buf.append(", " + att1.getSQLimage());
						}
						else{
							buf.append(" " + att1.getSQLimage());
							tmp_flag=0;
						}
					}
				}

				else {
					if(att1.getSQLimage().equals(SSQLparser.tmpXpath1)){
						String tmp = SSQLparser.Xpath.replace("\"", "'");
						tmp = tmp.replace("),", ")");
						buf.append(tmp);

						XMLFunction.xpath_first = 1;
					}

					else if(att1.getSQLimage().equals(SSQLparser.tmpXmlQuery1)){
						String tmp_xmlquery = new String();
						tmp_xmlquery = (SSQLparser.DB2_XQUERY.replace("\"", "'")).replace((SSQLparser.tmpXmlQuery2 + "',"), (SSQLparser.tmpXmlQuery2 + "' PASSING "));
						tmp_xmlquery = tmp_xmlquery.replace((SSQLparser.tmpXmlQuery1 + ")"), (SSQLparser.tmpXmlQuery1 + " AS \"a\")"));
						buf.append(tmp_xmlquery);

						XMLFunction.xpath_first = 1;
					}

					else{
						buf.append(att1.getSQLimage());
					}
				}
			}
			//ryuryu(end)//////////////////////////////////////////////////////////////////////////////////////////


			for (int j = 0; j < table_group.size(); j++) {
				if (((HashSet) (table_group.get(j))).containsAll(att1
						.getUseTables())) {
					tg1.addAll((HashSet) table_group.get(j));
				}
			}
		}
		Log.out("[tg1]" + tg1);

		// From
		flag = false;

		buf.append(" FROM ");

		//Iterator it = tg1.iterator();		//changed by goto 20120523

		Log.out("FROM_INFO:" + getFrom());

		//tk to use outer join////
		try{
			//changed by goto 20120523 start
			//���܂ł�SSQL�ł́A��ӂȑ����̏ꍇ�ł����Ă��K���������̑O��
			//�u�e�[�u����.�v��t����(qualify����)�K�v��������
			//���L�̕ύX�ɂ��A���̖������P����
			//�i����ɂ��A�ʏ��SQL���l�A���j�[�N�ȗ񖼂̑O�ɂ�qualification�͕s�v�ƂȂ�j
			buf.append(((FromParse) getFrom().getFromTable().get("")).getLine());

			/*while (it.hasNext()) {
				String tbl = (String) it.next();
				tbl = (String) it.next();

				Log.out("tbl:"+tbl);
				Log.out("buf@Make:"+ buf);

				if (flag) {
					buf.append(", "
						+ ((FromParse) from.getFromTable().get(tbl)).getLine());
				} else {
					flag = true;
					buf.append(((FromParse) from.getFromTable().get(tbl)).getLine());
				}
			}*/
			//changed by goto 20120523 end
			
		//tk to use outer join//////////////
		}catch(NullPointerException e){
			buf.append(getFrom().getLine());
		}
		//tk/////////////

		// Where
		flag = false;

		Iterator e2 = where.getWhereClause().iterator();
		while (e2.hasNext()) {
			WhereParse whe = (WhereParse) e2.next();
			if (tg1.containsAll(whe.getUseTables())) {
				if (flag) {
					buf.append(" AND " + whe.getLine());
				} else {
					flag = true;
					buf.append(" WHERE " + whe.getLine());
				}
			}
		}


		if (! GlobalEnv.getdbms().equals("db2")){
    		buf.append(";");
    	}

		return buf.toString();

	}

	public FromInfo getFrom() {
		return from;
	}

	public void setFrom(FromInfo from) {
		this.from = from;
	}

}