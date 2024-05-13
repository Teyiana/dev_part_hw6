SELECT NAME, PROJECT_COUNT
from (SELECT CLIENT_ID, count(CLIENT_ID) as PROJECT_COUNT  from PROJECT  group by CLIENT_ID) as cl_prj
join CLIENT on CLIENT.ID=cl_prj.CLIENT_ID
where PROJECT_COUNT = (SELECT count(CLIENT_ID) as COUNT from PROJECT  group by CLIENT_ID order by  COUNT desc limit 1)
group by NAME;