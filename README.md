Life Management System - opis projekta
Life Management System je desktop aplikacija razvijena u programskom jeziku Java, čiji je cilj da korisnicima omogući praćenje i organizaciju različitih aspekata svakodnevnog života na jednom mjestu. 
Aplikacija podržava višekorisnički rad, pri čemu svaki korisnik nakon prijave ima pristup isključivo vlastitim podacima.
Sistem je zamišljen modularno, tako da se sastoji od više nezavisnih funkcionalnih cjelina (trackera), koje su međusobno povezane kroz zajednički meni i korisničku sesiju.

Aplikacija je razvijena korištenjem:
programskog jezika Java,
Java Swing biblioteke za grafički interfejs,
MongoDB baze podataka za pohranu podataka,
MongoDB Java Driver-a za komunikaciju sa bazom,
Maven-a za upravljanje zavisnostima,
Git-a i GitHub-a za verzionisanje projekta.
GUI je izrađen pomoću IntelliJ IDEA GUI Designer-a, uz korištenje .form fajlova.

Projekat se preuzima sa GitHub repozitorija i otvara u IntelliJ IDEA razvojnom okruženju. Potrebno je imati instaliran JDK 11 ili noviji, kao i pokrenut MongoDB servis na lokalnoj adresi mongodb://localhost:27017.
Maven automatski preuzima potrebne biblioteke, uključujući MongoDB driver. Glavna klasa za pokretanje aplikacije je lifeapp.Main.

Aplikacija omogućava registraciju i prijavu korisnika. Prilikom registracije korisnik unosi korisničko ime, lozinku i odabire temu aplikacije. Podaci se čuvaju u MongoDB kolekciji users.
Prijava se vrši provjerom unesenih podataka u bazi, nakon čega se:
postavlja aktivna korisnička sesija,
učitava korisnička tema,
otvara glavni meni aplikacije.
Za upravljanje aktivnim korisnikom koristi se klasa Session.

Nakon uspješne prijave korisniku se prikazuje glavni meni (My Trackers), koji omogućava navigaciju između različitih modula aplikacije. 
Svi moduli se otvaraju unutar istog prozora, bez kreiranja dodatnih JFrame instanci.

Finance Tracker
Omogućava unos prihoda i rashoda, pregled svih transakcija u tabeli te automatski izračun ukupnih prihoda, rashoda i balansa. Podaci se čuvaju u kolekciji transactions.
Sleep Tracker
Omogućava unos broja sati spavanja, pregled unosa po danima i izračun prosječnog broja sati spavanja za prijavljenog korisnika. Podaci se čuvaju u kolekciji sleep_entries.
Habit Tracker
Omogućava dodavanje i brisanje navika, kao i pregled svih navika u tabeli. Sistem sprječava unos duplih navika za istog korisnika. Podaci se čuvaju u kolekciji habits.
Study Tracker
Omogućava praćenje sati učenja po danima, postavljanje sedmičnog cilja i pregled ukupnog broja sati učenja u odnosu na postavljeni cilj. Podaci se čuvaju u kolekcijama study_entries i study_goals.

Aplikacija podržava više tema (default, light i dark). Odabrana tema se čuva u bazi podataka i automatski se primjenjuje prilikom svake prijave korisnika. 
Upravljanje temama je centralizovano kroz klasu ThemeManager.

Life Management System predstavlja funkcionalnu i proširivu desktop aplikaciju koja demonstrira rad sa bazom podataka, višekorisnički sistem, modularnu arhitekturu i izradu grafičkog interfejsa u Javi. 
Projekat je osmišljen tako da se lako može nadograditi dodatnim modulima i funkcionalnostima u budućnosti.
