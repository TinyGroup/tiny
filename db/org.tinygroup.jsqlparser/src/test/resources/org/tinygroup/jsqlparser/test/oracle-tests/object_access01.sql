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

select
extractvalue(value(t), '/select_list_item/pos') + 1 pos,
extractvalue(value(t), '/select_list_item/value') res,
extractvalue(value(t), '/select_list_item/nonnulls') nonnulls,
extractvalue(value(t), '/select_list_item/ndv') ndv,
extractvalue(value(t), '/select_list_item/split') split,
extractvalue(value(t), '/select_list_item/rsize') rsize,
extractvalue(value(t), '/select_list_item/rowcnt') rowcnt,
extract(value(t), '/select_list_item/hash_val').getclobval() hashval
from
table
(
	xmlsequence
	(
		extract(:b1 , '/process_result/select_list_item')
	)
) t

