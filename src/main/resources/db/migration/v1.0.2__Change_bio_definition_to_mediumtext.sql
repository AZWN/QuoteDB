delimiter ;;

drop procedure if exists biography_medium_text();;
create procedure biography_medium_text ()
begin
    -- When table not exists there is no problem, requery will create it correct
    declare continue handler for 42102 begin end
    ALTER TABLE author CHANGE biography biography MEDIUMTEXT
end;;

call biography_medium_text();;
drop procedure if exists biography_medium_text();;