--
--  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
--
--  Licensed under the GPL, Version 3.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--       http://www.gnu.org/licenses/gpl.html
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  See the License for the specific language governing permissions and
--  limitations under the License.
--

select object_name, object_id,
 decode(status, 'INVALID', 'TRUE', 'FALSE') invalid, 
 'TRUE' runnable,
 plsql_debug
from sys.dba_objects o, dba_plsql_object_settings s
where o.owner = :schema
and s.owner = :schema
and s.name = o.object_name
and s.type = 'PACKAGE'
and object_type = 'PACKAGE'
and subobject_name is null
and object_id not in ( select purge_object from recyclebin )
and upper(object_name) in upper(:name)
 