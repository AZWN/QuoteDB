delimiter ;;

-- Drop when possible remained after error
DROP PROCEDURE IF EXISTS add_publication_year_column;;
DROP PROCEDURE IF EXISTS remove_publication_date_column;;

create procedure add_publication_year_column ()
begin
    declare continue handler for 1060 begin end;
    declare continue handler for 1146 begin end;
    alter table book add publicationYear bigint(20);
end;;

create procedure remove_publication_date_column ()
begin
    declare continue handler for 1146 begin end;
    declare continue handler for 1091 begin end;
    alter table book drop column publicationDate;
end;;

call add_publication_year_column();;
call remove_publication_date_column();;

-- Keep db clean
DROP PROCEDURE IF EXISTS add_publication_year_column;;
DROP PROCEDURE IF EXISTS remove_publication_date_column;;