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
  key ,
  num_val ,
  m_1
from
  t
model
  dimension by ( key )
  measures     ( num_val, 0 as m_1 )
  rules
  ( m_1[ 1 ] = num_val[ 1 ] * 10 ,
    m_1[ 2 ] = to_number( to_char( sysdate, 'YYYYMMDD' ) ),
    m_1[ 3 ] = dbms_utility.get_time ,
    m_1[ 4 ] = :bind_var
  )
order by
  key
