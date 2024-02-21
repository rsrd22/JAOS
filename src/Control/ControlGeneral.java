/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import BaseDeDatos.gestorMySQL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author richard
 */
public class ControlGeneral {
    private gestorMySQL gsql;
    
    public ControlGeneral() {
        gsql = new gestorMySQL();
    }
    
    public List<Map<String,String>> data_list(int caso,List<Map<String,String>> lista,String[] datos){
        List<Map<String,String>> rlista=new ArrayList<Map<String, String>>();
        try {            
            switch(caso){                
                case 1:{//para listar los datos por el dato enviado
                    for(Map<String,String> lis:lista){
                        boolean encontro=false;
                        for(Map<String,String> lr:rlista){
                            if(lis.get(datos[0]).equals(lr.get(datos[0]))){
                                encontro=true;
                                break;
                            }
                        }
                        if(!encontro){
                            rlista.add(lis);
                        }
                    }                    
                break;}
                case 10:{
                    for(Map<String,String> lis:lista){
                        boolean encontro=false, enc = true;
                        for(Map<String,String> lr:rlista){
                            boolean cond = false;
                            for(int i = 0; i < datos.length; i++){
                                if(lis.get(datos[i]).equals("")){
                                    cond = true;
                                    break;
                                }
                                if(i == 0){
                                    cond = lis.get(datos[i]).equals(lr.get(datos[i]));
                                }else{
                                    cond = cond && lis.get(datos[i]).equals(lr.get(datos[i]));
                                }
                            }                                
                            if(cond){
                                encontro = true;
                                break;
                            }
                        }
                        if(!encontro){
                            rlista.add(lis);
                        }
                    }
                    break;
                }
                case 2:{////para listar datos por la key mandada y el valor mandado
                    for(Map<String,String> lis:lista){
                        if(lis.get(datos[0]).equals(datos[1])){
                            rlista.add(lis);
                        }
                    }                    
                break;}   
                
                case 3:{ //para listar los datos por los datos enviados de de la siguiente forma
                    //k<->val, k<->val                    
                    for(Map<String,String> lis:lista){
                        int coincidencias=0;
                        for(String prm:datos){
                            String[] item=prm.split("<->");
                            if(lis.get(item[0]).equals(item[1])){
                                coincidencias++;
                            }
                        }                        
                        if(coincidencias==datos.length){
                            rlista.add(lis);
                        }
                    }                    
                break;}  
                
            }            
            
        }catch(Exception e){}
        return rlista;
    }  
  
    public List<Map<String,String>> data_list(int caso,List<Map<String,String>> lista,String[] datos,String[] datos2){
        List<Map<String,String>> rlista=new ArrayList<Map<String, String>>();
        try {
            switch(caso){  //////////////LISTAR DATOS POR VECTOR dE COiNCIDENCIAS              
                case 1:{
                    for(Map<String,String> lis:lista){
                        int coincidencias=0;  
                        for(String prm:datos2){
                            String[] item=prm.split("<->");
                            if(lis.get(item[0]).equals(item[1])){
                                coincidencias++;
                            }
                        }         
                        if(coincidencias==datos2.length){
                            boolean encontro=false;  
                            for(Map<String,String> lr:rlista){
                                
                                if(lis.get(datos[0]).equals(lr.get(datos[0])) || lis.get(datos[0]).trim().equals("")){
                                    encontro=true;
                                    break;
                                }
                            }
                            if(!encontro && !lis.get(datos[0]).trim().equals("")){
                                rlista.add(lis);
                            }
                        }
                    }                    
                break;}  
                case 10:{
                    for(Map<String,String> lis:lista){
                        int coincidencias=0;
                        for(String prm:datos2){
                            String[] item=prm.split("<->");
                            if(lis.get(item[0]).equals(item[1])){
                                coincidencias++;
                            }
                        }         
                        if(coincidencias==datos2.length){
                            boolean encontro=false, enc = true;
                            for(Map<String,String> lr:rlista){
                                boolean cond = false;
                                for(int i = 0; i < datos.length; i++){
                                    if(lis.get(datos[i]).equals("")){
                                        cond = true;
                                        break;
                                    }
                                    if(i == 0){
                                        cond = lis.get(datos[i]).equals(lr.get(datos[i]));
                                    }else{
                                        cond = cond && lis.get(datos[i]).equals(lr.get(datos[i]));
                                    }
                                }                                
                                if(cond){
                                    encontro = true;
                                    break;
                                }
                            }
                            if(!encontro){
                                rlista.add(lis);
                            }
                        }
                    }                    
                break;}
            }                        
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR data_list-->"+e.toString());}
        return rlista;
    }   

    public List<Map<String, String>> getMenu(String usuario) {
        String consulta = "SELECT pk_modulo AS IDMOD, nombre AS MODULO, tipo AS TPO, sub_menu AS SUB, \n" +
                            "IFNULL(s, '0') S, IFNULL(i, '0') I, IFNULL(u, '0') U, IFNULL(d, '0') D, IFNULL(v, '0') V\n" +
                            "FROM `menu_modulos` modu \n" +
                            "LEFT JOIN `usuarios_permisos` perm ON perm.`pfk_modulo` = modu.`pk_modulo` \n" +
                            "WHERE perm.`pfk_usuario` = '"+usuario+"'\n" +
                            "ORDER BY CAST(modu.`pk_modulo` AS UNSIGNED) ASC";
        
        List<Map<String, String>> lista = gsql.ListSQL(consulta);
        return lista;
    }
    
}
