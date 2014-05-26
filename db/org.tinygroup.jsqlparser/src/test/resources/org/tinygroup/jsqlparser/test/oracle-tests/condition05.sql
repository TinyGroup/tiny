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

select *
from t
where
 ( t.type = '2' ) or ( t.type = '3' )
 and  t.cde < 20 
 and  t.se = 'xxx' 
 and  t.id = '000000000002' 
 and ( ( t.sku_attr_1 is null ) or ( t.sku_attr_1 = '*' ) )
 and ( ( t.sku_attr_2 is null ) or ( t.sku_attr_2 = '*' ) )
 and ( ( t.sku_attr_3 is null ) or ( t.sku_attr_3 = '*' ) )
 and ( ( t.sku_attr_4 is null ) or ( t.sku_attr_4 = '*' ) )
 and ( ( t.sku_attr_5 is null ) or ( t.sku_attr_5 = '*' ) )
 and ( ( t.itype is null ) or ( t.itype = '*' ) )
 and ( ( t.bnbr is null ) or ( t.bnbr = '*' ) )
 and ( ( t.stat = '01' ) or ( t.stat = '*' ) )
 and ( ( t.orgn is null ) or ( t.orgn = '*' ) )
 and ( t.mbr = '0000000000001' )
 and ( t.nbr is null )


