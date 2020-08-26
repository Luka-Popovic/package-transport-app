
CREATE TABLE [Administrator]
( 
	[IdK]                integer  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

CREATE TABLE [Grad]
( 
	[IdG]                integer  IDENTITY ( 1,1 )  NOT NULL ,
	[Naziv]              varchar(100)  NULL ,
	[Post_broj]          varchar(100)  NULL 
)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdG] ASC)
go

CREATE TABLE [Korisnik]
( 
	[IdK]                integer  IDENTITY ( 1,1 )  NOT NULL ,
	[Ime]                varchar(100)  NULL ,
	[Prezime]            varchar(100)  NULL ,
	[Username]           varchar(100)  NULL ,
	[Password]           varchar(100)  NULL ,
	[JMBG]               varchar(100)  NULL ,
	[BrojPoslatih]       integer  NULL 
	CONSTRAINT [Default_Value]
		 DEFAULT  0
)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

CREATE TABLE [KorisnikZahtev]
( 
	[IdKZ]               integer  IDENTITY ( 1,1 )  NOT NULL ,
	[IdK]                integer  NOT NULL ,
	[IdV]                integer  NOT NULL 
)
go

ALTER TABLE [KorisnikZahtev]
	ADD CONSTRAINT [XPKKorisnikZahtev] PRIMARY KEY  CLUSTERED ([IdKZ] ASC)
go

CREATE TABLE [Kurir]
( 
	[IdK]                integer  NOT NULL ,
	[BrojIsporucenih]    integer  NULL 
	CONSTRAINT [Default_Value_170_1957659298]
		 DEFAULT  0,
	[Profit]             decimal(10,3)  NULL 
	CONSTRAINT [Default_Value_170_1230518658]
		 DEFAULT  0,
	[Status]             integer  NULL 
	CONSTRAINT [Default_Value_170_994654352]
		 DEFAULT  0,
	[IdV]                integer  NOT NULL 
)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

CREATE TABLE [Opstina]
( 
	[IdO]                integer  IDENTITY ( 1,1 )  NOT NULL ,
	[Naziv]              varchar(100)  NULL ,
	[x_kord]             integer  NULL ,
	[y_kord]             integer  NULL ,
	[IdG]                integer  NOT NULL 
)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([IdO] ASC)
go

CREATE TABLE [Paket]
( 
	[IdP]                integer  IDENTITY ( 1,1 )  NOT NULL ,
	[Status]             integer  NULL ,
	[Cena]               decimal(10,3)  NULL ,
	[VremePrihvatanja]   datetime  NULL ,
	[IdK]                integer  NULL 
)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdP] ASC)
go

CREATE TABLE [Ponuda]
( 
	[IdPonuda]           integer  IDENTITY ( 1,1 )  NOT NULL ,
	[Procenat]           integer  NULL ,
	[IdK]                integer  NOT NULL ,
	[IdP]                integer  NOT NULL 
)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([IdPonuda] ASC)
go

CREATE TABLE [Vozilo]
( 
	[IdV]                integer  IDENTITY ( 1,1 )  NOT NULL ,
	[RegBroj]            varchar(100)  NULL ,
	[TipGoriva]          integer  NULL ,
	[Potrosnja]          decimal(10,3)  NULL 
)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([IdV] ASC)
go

CREATE TABLE [Voznja]
( 
	[IdV]                integer  NOT NULL ,
	[IdP]                integer  NOT NULL ,
	[StatusVoznje]       integer  NULL ,
	[IdVoznja]           integer  IDENTITY ( 1,1 )  NOT NULL 
)
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [XPKVoznja] PRIMARY KEY  CLUSTERED ([IdVoznja] ASC)
go

CREATE TABLE [Zahtev]
( 
	[IdZ]                integer  IDENTITY ( 1,1 )  NOT NULL ,
	[TipPaketa]          integer  NULL ,
	[TezinaPaketa]       decimal(10,3)  NULL ,
	[IdOOd]              integer  NOT NULL ,
	[IdODo]              integer  NOT NULL ,
	[IdK]                integer  NOT NULL ,
	[IdP]                integer  NOT NULL ,
	[Distance]           decimal(10,3)  NULL 
	CONSTRAINT [Default_Value_170_552657229]
		 DEFAULT  0
)
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [XPKZahtev] PRIMARY KEY  CLUSTERED ([IdZ] ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [KorisnikZahtev]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [KorisnikZahtev]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([IdV]) REFERENCES [Vozilo]([IdV])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([IdV]) REFERENCES [Vozilo]([IdV])
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdG]) REFERENCES [Grad]([IdG])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([IdK]) REFERENCES [Kurir]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_19] FOREIGN KEY ([IdK]) REFERENCES [Kurir]([IdK])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_24] FOREIGN KEY ([IdP]) REFERENCES [Paket]([IdP])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_22] FOREIGN KEY ([IdV]) REFERENCES [Vozilo]([IdV])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_25] FOREIGN KEY ([IdP]) REFERENCES [Paket]([IdP])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([IdOOd]) REFERENCES [Opstina]([IdO])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([IdODo]) REFERENCES [Opstina]([IdO])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_18] FOREIGN KEY ([IdP]) REFERENCES [Paket]([IdP])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go




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