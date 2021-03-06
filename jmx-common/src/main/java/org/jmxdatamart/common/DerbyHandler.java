/*
 * Copyright (c) 2013, Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  o Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jmxdatamart.common;


import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Xiao Han
 * To change this template use File | Settings | File Templates.
 */
public class DerbyHandler extends DBHandler{
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private final String protocol = "jdbc:derby:";
    private final String tableSchema = "public";

    @Override
    public String getTableSchema() {
        return tableSchema;
    }

    public void shutdownDatabase(String databaseName){
        try
        {
            DriverManager.getConnection("jdbc:derby:" + databaseName + ";shutdown=true");

        }
        catch (SQLException se)
        {
            if (( (se.getErrorCode() == 45000)
                    && ("08006".equals(se.getSQLState()) ))) {
                // we got the expected exception
                System.out.println("Derby shut down normally");
                // Note that for single database shutdown, the expected
                // SQL state is "08006", and the error code is 45000.
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");

            }
        }
    }

    @Override
    public Connection connectDatabase(String databaseName,java.util.Properties p) {
        try{
            return DriverManager.getConnection(protocol + databaseName + ";create=true", p);
        }
        catch (SQLException se){
            logger.error("Can't create the Derby database:" + se.getMessage(), se);
            throw new RuntimeException(se);
        }
    }



    @Override
    public boolean databaseExists(String databaseName,java.util.Properties p){
        //Maybe it is a dummy way to check if a db exits, need to improve
        try {
            DriverManager.getConnection(protocol+databaseName+ ";create=false", p);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getDriver() {
        return driver;
    }







}
