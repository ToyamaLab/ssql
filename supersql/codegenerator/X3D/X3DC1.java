package supersql.codegenerator.X3D;

import java.util.Vector;

import supersql.codegenerator.Connector;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class X3DC1 extends Connector{
    Manager manager;

    X3DEnv x3d_env;
    
    X3DValue value;

    public X3DC1(Manager manager, X3DEnv lenv) {
        this.manager = manager;
        this.x3d_env = lenv;
    }

    @Override
	public String work(ExtList data_info) {
        Vector vector_local = new Vector();
        Log.out("------- C1 -------");
        Log.out("tfes.contain_itemnum=" + tfes.contain_itemnum());
        Log.out("tfes.size=" + tfes.size());
        Log.out("countconnetitem=" + countconnectitem());
        this.setDataList(data_info);


        int i = 0;
        value = new X3DValue();

        while (this.hasMoreItems()) {
        		ITFE tfe = (ITFE) tfes.get(i);

                this.worknextItem();
        		
                if (X3DEnv.x3d_add == 1) {
                	if (X3DEnv.groupflag == 0) {
                		if (X3DEnv.linkflag == 1) {
                			X3DEnv.linkflag = 0;
                			X3DEnv.bookcount++;
                		}
                		else {
                			if (X3DEnv.bookcount%8 == 0) {
                				if (X3DEnv.bookcount == 40) {
                					if (X3DEnv.tateflag == 1) {
                						X3DEnv.x += 4.0;
                						X3DEnv.bx = X3DEnv.x - 0.2;
                						X3DEnv.by = X3DEnv.y - 0.1;
                						if (X3DEnv.x > X3DEnv.xmax) {
                							X3DEnv.xmax = X3DEnv.x;
                							X3DEnv.width = X3DEnv.x - X3DEnv.xi + 20.0;
                							X3DEnv.lx[X3DEnv.lcount] = X3DEnv.width/2.0 - 12.0 + X3DEnv.xi;
                							X3DEnv.rwx = X3DEnv.lx[X3DEnv.lcount] + X3DEnv.width/2.0 - 0.5;
                							X3DEnv.lwx = X3DEnv.lx[X3DEnv.lcount] - X3DEnv.width/2.0 + 0.5;
                							if(X3DEnv.lcount == 0){ X3DEnv.l = X3DEnv.lwx;}
                							if(X3DEnv.rwx > X3DEnv.r){ X3DEnv.r = X3DEnv.rwx;}
                							X3DEnv.fwx = 0.5 + (X3DEnv.width-13.0)/2.0 + X3DEnv.xi;
                							X3DEnv.bwx = X3DEnv.lx[X3DEnv.lcount];
                						}
                						X3DEnv.bookcount = 1;
                						X3DEnv.addflag = 1;
                					}
                					if (X3DEnv.yokoflag == 1) {
                        
                					}  		
                				}	
                				else {
                					X3DEnv.bx = X3DEnv.x - 0.2;
                        			X3DEnv.by += value.y;
                        			X3DEnv.bookcount++;
                				}
                			}
                			else {
                				X3DEnv.bx += value.book_width;
                				X3DEnv.bookcount++;
                			}
                			X3DEnv.linkflag = 1;
                		}
                	}
                }
                else {
                	X3DEnv.x = X3DEnv.rwx + 12.0 ;
                	X3DEnv.xi = X3DEnv.x;
                	X3DEnv.ly[X3DEnv.lcount] = X3DEnv.ly[X3DEnv.lcount-1];
                	X3DEnv.y = value.y_ini;
                	X3DEnv.z = value.z_ini;
                }

        }
        i++;
		return null;
    }

    @Override
	public String getSymbol() {
        return "X3DC1";
    }

}
