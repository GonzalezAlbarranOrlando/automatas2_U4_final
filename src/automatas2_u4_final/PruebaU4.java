/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatas2_u4_final;

import java.util.StringTokenizer;

/**
 *
 * @author 52722
 */
public class PruebaU4 {
    
    
    static String[] arrTemp = new String[100];
    static String[] arrOp1 = new String[100];
    static String[] arrOp2 = new String[100];
    static String[] arrOp = new String[100];
    static int contMsg=0;

    
    
    static String encabezado="TITLE codigo prueba\n" +
".MODEL SMALL\n" +
".STACK 64\n";    
    static String data=".data\n";
    static String code="\n.code\n"
            + "mov ax, @data\n" +
"mov ds,ax\n\n";
    static String codigoE;
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here     
        /*
        asignacionData("B","5");
        asignacionData("C","6");
        asignacionData("A","?");
        suma("B","C","A");
        */
        
        String s;
        s= "			A\n" +
"			B\n" +
"			C\n" +
"'DAME EL PRIMER VALOR DE B:'  	 	 	WRITE\n" +
"B 	 	 	READ\n" +
"'DAME EL PRIMER VALOR DE C:'  	 	 	WRITE\n" +
"C 	 	 	READ\n" +
"B	C	+	T1\n" +
"T1  	 	 = 	 A\n" +
"'EL RESULTADO DE LA SUMA ES:'  	 	 	WRITE\n" +
"A  	 	 	WRITE";
        
        System.out.println("--------------------------");
        System.out.println(s);
        System.out.println("--------------------------");
        String[][] m=tabla_to_mat(s);
        imprimirMatriz(m); 
        System.out.println("--------------------------");
        //m=quitarEspaciosDeSobra(m);
        imprimirMatriz(m);
        System.out.println("--------------------------");
        codigoEnsamblador(m);
        codigoE=encabezado+data+code+".exit\nend";
        System.out.println(codigoE);
    }
    static void codigoEnsamblador(String[][]m){
        for (int i = 0; i < m.length; i++) {
            if (m[i][2].contains("+")) { 
                suma(m[i][0],m[i][1],m[i][3]);
            }else if (m[i][2].contains("-")) { 
                resta(m[i][0],m[i][1],m[i][3]);
            }else if (m[i][2].contains("*")) { 
                mul(m[i][0],m[i][1],m[i][3]);
            }else if (m[i][2].contains("/")) { 
                div(m[i][0],m[i][1],m[i][3]);
            }else if (m[i][2].contains("<")||m[i][2].contains(">")) { 
                condicion(m[i][0],m[i][1],m[i][2],m[i][3]);
            
            
            
            
            
            }else if(m[i][3].contains("READ")){ //READ A
                read(m[i][0]);
            }else if(m[i][3].contains("WRITE")){ //WRITE A || WRITE 'DAME EL PRIMER VALOR DE C:'
                if (m[i][0].contains("'")) {  //WRITE 'DAME EL PRIMER VALOR DE C:'
                    
                    writeMsg(asignacionDataMsg(m[i][0]));                     
                }else{//WRITE A 
                    writeVar(m[i][0]);                     
                }
            }                                                      
            else if(m[i][3].contains(":")){ //10: 
                code+=m[i][3]+"\n\n";
            }else if(validacionNum(m[i][3])){ //goto 10
                code+="jmp "+m[i][3]+"\n\n";
                
            
  
            }
           
            
            else if(m[i][0].equals("")||m[i][1].equals("")||m[i][2].equals("")){ // INT A
                asignacionData("?",m[i][3]); 
            }
            
            
            
            
            if(validarNombreTemp(m[i][3])){ // B C + T1
                asignacionData("?",m[i][3]); 
            }
            
            
            
        }
        
        
    }
    
        
    static void suma(String op1,String op2,String res){
        code+=";realizar suma\n";
        code+="mov al,"+op1+"\n";
        code+="add al,"+op2+"\n";
        code+="mov "+res+",al\n\n";        
    }
    static void resta(String op1,String op2,String res){
        code+=";realizar resta\n";
        code+="mov al,"+op1+"\n";
        code+="sub al,"+op2+"\n";
        code+="mov "+res+",al\n\n";        
    }  
    static void mul(String op1,String op2,String res){
        code+=";realizar mul\n";
        code+="mov al,"+op1+"\n";
        code+="mov bl,"+op2+"\n";
        code+="mul bl\n";
        code+="mov "+res+",al\n\n";        
    }
    static void div(String op1,String op2,String res){
        code+=";realizar div\n";
        code+="xor ax,ax\n";
        code+="mov al,"+op1+"\n";
        code+="mov bl,al\n";
        code+="mov al,"+op2+"\n";
        code+="div bl\n";
        code+="mov bl,al\n";
        code+="mov "+res+",al\n\n";        
    }
    static void condicion(String op1,String op2,String op,String res){
        code+=";realizar comparacion\n";
        if (op.equals(">")) {
            op="ja";
        }else{
            op="jb";
        }
        code+="mov cl,"+op1+"\n";
        code+="cmp cl,"+op2+"\n";
        code+=op+" "+res+"\n\n";
    }

    static void writeVar(String var){
        code+=";escribir una variable\n";
        code+="mov dl,"+var+"\n";
        code+="add dl,30h\n";
        code+="mov ah,02h\n";
        code+="int 21h\n\n";       
    }
    static void writeMsg(String msg){
        code+=";escribir un mensaje\n";
        code+="mov ah,09h\n";
        code+="lea dx,"+msg+"\n";
        code+="int 21h\n\n";        
    }
    static void read(String op1){
        code+=";leer un \n";
        code+="mov ah, 01h\n";
        code+="int 21h\n";
        code+="sub al,30h\n";
        code+="mov "+op1+",al\n\n";
    }     
    static void asignacionData(String op1,String res){        
        data+=res+" db "+op1+"\n";                
    }
    static String asignacionDataMsg(String op1){
        String msg=newMsg();
        data+=msg+" db "+op1+",10,13,'$'\n";
        return msg;
    }
    static String newMsg() {
        return "msg" + Integer.toString(++contMsg);
    }
    
    
    static boolean validarNombreTemp(String s){
        if (!s.startsWith("T")) {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            if (!"1234567890".contains(String.valueOf(s.charAt(i)))) { 
                return false;
            }
        }
        return true;
    }
    
    
    
    
    
        static String[][] tabla_to_mat(String s) {
        StringTokenizer t = new StringTokenizer(s, "\n");
        String[][] matriz = new String[t.countTokens()][4];

        for (int i = 0; i < matriz.length; i++) {
            StringTokenizer t_aux = new StringTokenizer(t.nextToken(), "\t");
            switch (t_aux.countTokens()) {
                case 4:
                    //t1=a+b;
                    matriz[i][0] = t_aux.nextToken();
                    matriz[i][1] = t_aux.nextToken();
                    matriz[i][2] = t_aux.nextToken();
                    matriz[i][3] = t_aux.nextToken();
                    break;
                case 3:
                    // z = t1;
                    matriz[i][0] = t_aux.nextToken();
                    matriz[i][1] = "";
                    matriz[i][2] = t_aux.nextToken();
                    matriz[i][3] = t_aux.nextToken();
                    break;
                case 2:
                    // write t1; read a;
                    matriz[i][0] = t_aux.nextToken();
                    matriz[i][1] = "";
                    matriz[i][2] = "";
                    matriz[i][3] = t_aux.nextToken();
                    break;
                case 1:
                    // write t1; read a;
                    matriz[i][0] = "";
                    matriz[i][1] = "";
                    matriz[i][2] = "";
                    matriz[i][3] = t_aux.nextToken();
                    break;
                default:
                    break;
            }
            
        }
        return matriz;
    }
    
        
    static void imprimirMatriz(String[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + "$");	// Imprime elemento
            }
            System.out.println();	// Imprime salto de línea
        }
    }
    
        static boolean validacionNum(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!"1234567890".contains(Character.toString(s.charAt(i)))) {
                return false;
            }
        }
        return true;
    }
    
    
    
    
}
