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

(  select "x"."r_no",   
          "x"."i_id",   
	  "x"."ind",	
	  "x"."item",   
	  '0' "o"
   from "x"        
   where ("x"."r_no" = :a)
   minus
   select "y"."r_no",   
          "y"."i_id",
 	  "y"."ind",	   
 	  "y"."item",   
 	  '0' "o"  
   from "y"
   where ("y"."r_no" = :a)
)
union
(  select "y"."r_no",   
          "y"."i_id",   
 	  "y"."ind",	
 	  "y"."item",   
 	  '1' "o"  
   from "y"
   where ("y"."r_no" = :a)
   minus
   select "x"."r_no",   
          "x"."i_id",
          "x"."ind",   
          "x"."item",   
	  '1' "o"  
    from "x"
    where ("x"."r_no" = :a)
)
order by 4,3,1

