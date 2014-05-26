/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.jsqlparser.schema;

import org.tinygroup.jsqlparser.expression.*;
import org.tinygroup.jsqlparser.statement.select.*;

/**
 * A table. It can have an alias and the schema name it belongs to.
 */
public class Table implements FromItem, MultiPartName {

    private Database database;
    private String schemaName;
    private String name;

    private Alias alias;
    private Pivot pivot;

    public Table() {
    }

    public Table(String name) {
        this.name = name;
    }

    public Table(String schemaName, String name) {
        this.schemaName = schemaName;
        this.name = name;
    }

    public Table(Database database, String schemaName, String name) {
        this.database = database;
        this.schemaName = schemaName;
        this.name = name;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String string) {
        schemaName = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        name = string;
    }


    public Alias getAlias() {
        return alias;
    }


    public void setAlias(Alias alias) {
        this.alias = alias;
    }


    public String getFullyQualifiedName() {
        String fqn = "";

        if (database != null) {
            fqn += database.getFullyQualifiedName();
        }
        if (!(fqn==null||fqn.length()==0)) {
            fqn += ".";
        }

        if (schemaName != null) {
            fqn += schemaName;
        }
        if (!(fqn==null||fqn.length()==0)) {
            fqn += ".";
        }

        if (name != null) {
            fqn += name;
        }

        return fqn;
    }


    public void accept(FromItemVisitor fromItemVisitor) {
        fromItemVisitor.visit(this);
    }

    public void accept(IntoTableVisitor intoTableVisitor) {
        intoTableVisitor.visit(this);
    }


    public Pivot getPivot() {
        return pivot;
    }


    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }


    public String toString() {
        return getFullyQualifiedName()
               + ((pivot != null) ? " " + pivot : "")
               + ((alias != null) ? alias.toString() : "");
    }
}
