# Housework management system
Aplikacja do zarządzania obowiązkami domowymi przy wynajmie mieszkania.

### Funkcjonalności:
1. Dodawanie i edycja mieszkań przez właściciela.
2. Dodawanie i edycja obowiązków domowych przez właściciela mieszkania.
3. Przypisywanie się do wykonania zadania.
4. Rezygnacja z wykonania zadania.
5. Wykonanie zadania.
6. Wystawianie oceny za wykonane zadanie.

### Budowanie
- <b>mvn spring-boot:run</b>
- baza danych h2 in memory, po wyłączeniu aplikacji dane znikają
- skrypt inicjujący przykładowe dane dla aplikacji znajduje się w katalogu:SCIN_PS_20210705223208 housework-management-system\src\main\resources\import.sql. Skrypt jest ładowany każdorazowo przy uruchomieniu aplikacji.

### Dane do logowania
- właściciel mieszkania:
admin/password
- użytkownik/lokator:
user/password
