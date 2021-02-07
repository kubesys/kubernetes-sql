/*

 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.sql.clients;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.github.kubesys.sql.KubeSQLClient;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * 
 * @version 1.2.0
 * @since 2020/4/23
 *
 */
public class MysqlClient extends KubeSQLClient {

	public static final String CHECK_DATABASE  = "SELECT * FROM information_schema.SCHEMATA where SCHEMA_NAME='#DATBASE#'";
	
	public static final String CREATE_DATABASE = "CREATE DATABASE #DATBASE# CHARACTER SET utf8 COLLATE utf8_general_ci";
	
	
	public static final String CHECK_TABLE     = "SELECT DISTINCT t.table_name, n.SCHEMA_NAME FROM "
												+ "information_schema.TABLES t, information_schema.SCHEMATA n "
												+ "WHERE t.table_name = '#TABLE#' AND n.SCHEMA_NAME = '#DATBASE#'";
	
	public static final String CREATE_TABLE    = "CREATE TABLE #TABLE# (name varchar(512), namespace varchar(128), time bigint,"
												+ "data json, primary key(name, namespace)) DEFAULT CHARSET=utf8";
	
	private static final String CONDITION = " JSON_EXTRACT(data, '$.#ITEM#') like '%#VALUE#%' AND ";
	
	private static final String LIMIT = " LIMIT #FROM#, #TO#";
	
	public static final String DEF_DRV  = "com.mysql.cj.jdbc.Driver";

	public static final String DEF_URL  = "jdbc:mysql://kube-database.kube-system:3306?useUnicode=true&characterEncoding=UTF8&connectTimeout=2000&socketTimeout=6000&autoReconnect=true&&serverTimezone=Asia/Shanghai";

	public static final String DEF_USER = "root";

	public MysqlClient(DruidPooledConnection conn) throws Exception {
		super(conn);
	}

	public MysqlClient(DruidPooledConnection conn, String database) throws Exception {
		super(conn, database);
	}

	@Override
	public String checkDatabaseSql() {
		return CHECK_DATABASE;
	}

	@Override
	public String createDatabaseSql() {
		return CREATE_DATABASE;
	}

	@Override
	public String checkTableSql() {
		return CHECK_TABLE;
	}

	@Override
	public String createTableSql() {
		return CREATE_TABLE;
	}

	@Override
	public String defaultDriver() {
		return DEF_DRV;
	}

	@Override
	public String defaultUrl() {
		return DEF_URL;
	}

	@Override
	public String defaultUser() {
		return DEF_USER;
	}


	@Override
	public String queryConditions() {
		return CONDITION;
	}

	
	@Override
	public String getUrlPrefix() {
		return "jdbc:mysql://";
	}

	@Override
	public String queryLimit(int limit, int page) {
		int l = (limit <= 0) ? 10 : limit;
		int p = (page <= 1) ? 1 : page;
		return LIMIT.replace("#FROM#", String.valueOf((p - 1) * l)).replace("#TO#", String.valueOf(l));
	}


}
