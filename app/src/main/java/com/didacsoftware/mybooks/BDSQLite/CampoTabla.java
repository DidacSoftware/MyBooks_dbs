package com.didacsoftware.mybooks.BDSQLite;

public class CampoTabla {


    public static final String TABLA_BOOKS="tbl_books";
    public static final String BOOKS_iID="id_";
    public static final String BOOKS_sAUTHOR="campo1";
    public static final String BOOKS_sDESCRIPTION="campo2";
    public static final String BOOKS_sPUBLICATION_DATE="campo3";
    public static final String BOOKS_sTITLE="campo4";
    public static final String BOOKS_sURL_IMAGE="campo5";


    public static final String CREAR_TABLA_BOOKS="CREATE TABLE " +TABLA_BOOKS+" ("
            +BOOKS_iID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +BOOKS_sAUTHOR+" TEXT, "
            +BOOKS_sDESCRIPTION+" TEXT, "
            +BOOKS_sPUBLICATION_DATE+" TEXT, "
            +BOOKS_sTITLE+" TEXT, "
            +BOOKS_sURL_IMAGE+" TEXT"
            +")";
    
}
