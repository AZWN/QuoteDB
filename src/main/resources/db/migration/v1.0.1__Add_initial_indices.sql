delimiter ;;

drop procedure if exists add_indices();;

create procedure add_indices ()
begin
    -- When table not exists there is no problem, requery will create it correct
    declare continue handler for 42102 begin end;
    CREATE INDEX author_firstName ON author(firstName);
    CREATE INDEX author_lastName ON author(lastName);

    CREATE INDEX book_title ON book(title);
    CREATE INDEX book_publisher ON book(publisher);

    CREATE INDEX bookquote_title ON bookquote(title);
    CREATE INDEX quickquote_title ON quickquote(title);
end;;

call add_indices ();;
drop procedure if exists add_indices();;