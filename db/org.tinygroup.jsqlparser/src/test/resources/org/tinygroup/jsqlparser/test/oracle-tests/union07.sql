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

select * from (
        select row_.*, rownum rownum_
        from (
                select *
                        from
                        (
                                select results.*,row_number() over ( partition by results.object_id order by results.gmt_modified desc) rn
                                from
                                (
                                        (
                                                select                                        sus.id                id,          sus.gmt_create        gmt_create,
                                                        sus.gmt_modified      gmt_modified,          sus.company_id        company_id,
                                                        sus.object_id         object_id,          sus.object_type       object_type,
                                                        sus.confirm_type      confirm_type,          sus.operator          operator,
                                                        sus.filter_type       filter_type,          sus.member_id         member_id,
                                                        sus.member_fuc_q        member_fuc_q,          sus.risk_type         risk_type     , 'y' is_draft
                                                from                        f_u_c_ sus , a_b_c_draft p                                                              ,
                                                        member m
                                                where 1=1                                               and             p.company_id = m.company_id
                                                        and                     m.login_id=?
                                                        and p.sale_type in(                     ?                       )
                                                        and p.id=sus.object_id
                                        )
                                        union
                                        (
                                                select                                        sus.id                id,          sus.gmt_create        gmt_create,
                                                        sus.gmt_modified      gmt_modified,          sus.company_id        company_id,
                                                        sus.object_id         object_id,          sus.object_type       object_type,
                                                        sus.confirm_type      confirm_type,          sus.operator          operator,
                                                        sus.filter_type       filter_type,          sus.member_id         member_id,
                                                        sus.member_fuc_q        member_fuc_q,          sus.risk_type         risk_type     , 'n' is_draft
                                                from f_u_c_ sus , a_b_c p                                                                   ,member m
                                                where 1=1
                                                        and             p.company_id = m.company_id
                                                        and                     m.login_id=?
                                                        and p.sale_type in(                     ?                       )
                                                        and p.id=sus.object_id
                                        )
                                        ) results
                                )               where rn = 1 order by gmt_modified desc
                        )row_ where rownum <= ?
        )
where rownum_ >= ?
