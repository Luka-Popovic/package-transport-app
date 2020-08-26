create procedure ProveraVozilaKurira
@IdK int,
@IdV int,
@OutPutValue int output
as
begin

	declare @brojKuriraZaVozilo int -- proverava da li vise kurira koristi isto vozilo
	declare @brojVozilaPoKuriru int -- proverava da li kurir ima vise vozila

	set @brojKuriraZaVozilo  = 0
	set @brojVozilaPoKuriru  = 0
	set @OutPutValue = 0

	--proveravam da li se u tabeli Kurir nalazi vozilo koje hoce Kurir koga ubacujemo da vozi
	select @brojKuriraZaVozilo = Count(*) from Kurir where IdV = @IdV --group by IdK
	
	 if(@brojKuriraZaVozilo  > 0)
	 begin
		set @OutPutValue = 1
	 end

	 --proveravam da li kurir koga ubacujem vec poseduje neko vozilo
	 select @brojVozilaPoKuriru = Count(*) from (Select IdV from Kurir where IdK = @IdK group by IdV) as Z

	 	 if(@brojVozilaPoKuriru  > 0)
	 begin
		set @OutPutValue = 1
	 end

end
go

create procedure UpdateTime
@IdP int
as
begin

	update Paket set VremePrihvatanja = GETDATE() where IdP = @IdP

end
go