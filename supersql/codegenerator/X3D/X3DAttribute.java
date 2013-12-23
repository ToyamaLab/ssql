package supersql.codegenerator.X3D;

import supersql.codegenerator.Attribute;
import supersql.codegenerator.Manager;
import supersql.common.Log;
import supersql.extendclass.ExtList;

public class X3DAttribute extends Attribute{
	
    Manager manager;

    X3DEnv x3d_env;
    
    X3DValue value;
    
    int i = 0;

    public X3DAttribute(Manager manager, X3DEnv lenv) {
        super();
        this.manager = manager;
        this.x3d_env = lenv;
    }

    @Override
	public String work(ExtList data_info) {
        value = new X3DValue();
        X3DEnv.object = this.decos.getStr("object");
        
        if (X3DEnv.x3d_add == 1) {
        	X3DEnv.vx = X3DEnv.x - 4.0;
        	X3DEnv.vz = X3DEnv.z + 5.0;
        	X3DEnv.vy = X3DEnv.y + 3.5;
        	
        	if (X3DEnv.groupflag == 1) {
        		x3d_env.code.append("Sign{position " + (X3DEnv.x-3.0) + " " + (X3DEnv.y+7.0) + " " + X3DEnv.z + " \n");
        		x3d_env.code.append("image \"" + this.getStr(data_info) + ".jpg\"}\n");
        		x3d_env.code.append("DEF view" + i++ + " Viewpoint{\n");
        		x3d_env.code.append("position " + X3DEnv.vx + " " + X3DEnv.vy + " " + X3DEnv.vz + "\n");
        		x3d_env.code.append("orientation 0 1 0 -0.78\n");
        		x3d_env.code.append("description \"" + this.getStr(data_info) + "\"");
        		x3d_env.code.append("}\n");
        		X3DEnv.groupflag = 0;
        	}
        	else { 
        		if(X3DEnv.linkflag == 0) {
        			if (X3DEnv.addflag == 1) {
        	         
        				x3d_env.code.append("Shelf2{position " + X3DEnv.x + " " + X3DEnv.y + " " + X3DEnv.z + " }\n");
        				Log.out("Shelf2{position " + X3DEnv.x + " " + X3DEnv.y + " " + X3DEnv.z + " }");
        	            X3DEnv.addflag = 0;
        			}
        			x3d_env.code.append("CD {position " + X3DEnv.bx + " " + X3DEnv.by + " " + X3DEnv.bz + " image \"");
        			Log.out("CD{position " + X3DEnv.bx + " " + X3DEnv.by + " " + X3DEnv.bz + " image \"");
        			x3d_env.code.append(this.getStr(data_info) + "\"");
        			Log.out(this.getStr(data_info) + "\"");
        			
        		}
        		else {
        			x3d_env.code.append(" data \"");
        			Log.out(" data \"");
        			x3d_env.code.append(this.getStr(data_info) + "\"}\n");
        			Log.out(this.getStr(data_info) + "\"}\n");	
        		}
        	}
        }
		return null;
    }    
}