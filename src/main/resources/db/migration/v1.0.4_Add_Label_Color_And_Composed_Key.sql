delimiter ;;

drop procedure if exists alter_labels;;

create procedure alter_labels ()
begin
    declare continue handler for 42102 begin end;
    declare continue handler for 1146 begin end;
    -- Dont error when column does already exist
    declare continue handler for 1060 begin end;
    -- Dont error when index does already exist
    declare continue handler for 1061 begin end;
    -- Dont error when labelNam index does not exist
    declare continue handler for 1091 begin end;

    alter table label add column color varchar(31);
    -- Remove index, and add again without unique constraint.
    alter table label drop index labelName;
    alter table label add index labelName (labelName);
    alter table label add unique index user_labelName_unique (`user`, labelName);
end;;

call alter_labels ();;
drop procedure if exists alter_labels;;