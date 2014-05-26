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

insert first
when customer_id < 'i' then
  into cust_ah
  values (customer_id, program_id, delivered_date)
when customer_id < 'q' then
  into cust_ip
  values (customer_id, program_id, delivered_date)
when customer_id > 'pzzz' then
  into cust_qz
  values (customer_id, program_id, delivered_date)
select program_id, delivered_date, customer_id, order_date
from airplanes
