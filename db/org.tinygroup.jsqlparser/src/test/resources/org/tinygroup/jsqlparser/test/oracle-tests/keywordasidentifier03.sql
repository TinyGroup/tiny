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

select ind.index_owner,ind.index_name,ind.uniqueness
 , ind.status,ind.index_type,ind.temporary, ind.partitioned,ind.funcidx_status
 , ind.join_index,ind.columns,ie.column_expression
 , ind.index_name sdev_link_name,'INDEX' sdev_link_type, ind.index_owner sdev_link_owner     
from 
(
   select index_owner,table_owner,index_name,uniqueness, status,index_type,temporary, partitioned,funcidx_status, join_index
   , max(decode(position,1 ,column_name))|| max(decode(position,2 ,', '||column_name))|| max(decode(position,3 ,', '||column_name))|| max(decode(position,4 ,', '||column_name))|| max(decode(position,5 ,', '||column_name))||           max(decode(position,6 ,', '||column_name))||           max(decode(position,7 ,', '||column_name))||           max(decode(position,8 ,', '||column_name))||           max(decode(position,9 ,', '||column_name))||           max(decode(position,10,', '||column_name)) columns 

  from
  (
     select di.owner index_owner,dc.table_owner,dc.index_name,di.uniqueness, di.status, di.index_type, di.temporary, di.partitioned,di.funcidx_status, di.join_index
     , dc.column_name,dc.column_position position          
     from all_ind_columns dc,all_indexes di         
     where dc.table_owner = :OBJECT_OWNER           
     and dc.table_name  =  :OBJECT_NAME           
     and dc.index_name = di.index_name           
     and dc.index_owner = di.owner 
  )    
  group by index_owner,table_owner,index_name,uniqueness, status, index_type, temporary, partitioned,funcidx_status, join_index
) ind,
ALL_IND_EXPRESSIONS ie  
where ind.index_name = ie.index_name(+)
and ind.index_owner = ie.index_owner(+)
