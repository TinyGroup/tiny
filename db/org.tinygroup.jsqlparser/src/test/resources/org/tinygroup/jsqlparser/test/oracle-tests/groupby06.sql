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
prod_category, prod_subcategory, country_id, cust_city, count(*)
   from  products, sales, customers
   where sales.prod_id = products.prod_id 
   and sales.cust_id=customers.cust_id 
   and sales.time_id = '01-oct-00'
   and customers.cust_year_of_birth between 1960 and 1970
group by grouping sets 
  (
   (prod_category, prod_subcategory, country_id, cust_city),
   (prod_category, prod_subcategory, country_id),
   (prod_category, prod_subcategory),
    country_id
  )
order by prod_category, prod_subcategory, country_id, cust_city
