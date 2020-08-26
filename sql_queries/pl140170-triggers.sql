create trigger TR_TransportOffer_PrihvacenZahtev_Insert
on Voznja
for INSERT, UPDATE
as
begin

	declare @IdP int
	declare @cursor cursor

	set @cursor = cursor for select IdP from inserted
	open @cursor
	
	fetch next from @cursor into @IdP

	while @@FETCH_STATUS = 0
	begin
	
	delete from Ponuda where IdP = @IdP -- brisemo sve ponude za taj paket

	fetch next from @cursor into @IdP
	end

	close @cursor
	deallocate @cursor

end
go

/*
create trigger TR_TransportOffer_PrihvacenZahtev_Update
on Voznja
for Update
as
begin

	declare @IdP int
	
	select @IdP = IdP from inserted

	delete from  Ponuda where IdP = @IdP  -- brisemo sve ponude za novi paket

end
go
*/

/*nema sta da se radi za delete prihvacenog zahteva jer su sve ponude vec obrisane za tu poruku
create trigger TR_TransportOffer_PrihvacenZahtev_Delete
on Voznja
for Delete
as
begin

	declare @IdPonuda int

	select @IdPonuda = IdPonuda from deleted

	delete from  Ponuda where IdPonuda = @IdPonuda   -- brisemo ponudu koju smo izmenili jer je onda jedina ostala za taj paket
													 -- jer kada se radio njen insert obrisane su sve ostale ponude za taj paket

end
*/