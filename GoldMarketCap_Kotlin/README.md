# GoldMarketCap

GoldMarketCap, Kotlin dili kullanılarak geliştirilen bir Android uygulamasıdır ve gerçek zamanlı altın fiyat takibi ve piyasa verilerini sağlamak için tasarlanmıştır.

## Özellikler

<<<<<<< HEAD
- **`MainActivity.kt`**: Uygulamanın ana ekranını ve işlevselliğini yönetir.
- **`GoldPricesAPI.kt`**: Altın fiyatlarını çeken REST API servisidir.
- **`RecyclerViewAdapter.kt`**: Listeleme işlemleri için adaptör.
- **`activity_main.xml`** ve **`row_layout.xml`**: Arayüz tasarım dosyaları.

  <img width="336" alt="ornek" src="https://github.com/mulosbron/GoldMarketCap/assets/91866065/6e39356a-7caa-45da-bcfe-4a8d8d5bb408">
=======
- Canlı altın piyasa fiyatlarını ve yüzdelik değişimleri çekme ve gösterme.
- Gerçek zamanlı veri elde etmek için REST API servislerini kullanma.
- RecyclerView ile verileri sunma.

## Temel Bileşenler

- **`MainActivity.kt`**: Uygulamanın ana ekranını yönetir ve altın piyasaları fiyatlarını gösterir.
- **`RegisterActivity.kt`**: Yeni kullanıcıların üyelik işlemlerini yönetir.
- **`LoginActivity.kt`**: Kullanıcıların hesaplarına giriş yapmalarını sağlar.
- **`FooterActivity.kt`**: Navigasyonu kontrol eder ve uygulamanın farklı bölümlerinden erişilebilen ortak özellikleri gösterir.

## Modeller

- **`DailyPercentage.kt`**: Altın fiyatlarındaki günlük yüzdelik değişimleri temsil eden veri modeli.
- **`GoldPrice.kt`**: Altın fiyatlarını temsil eden veri modeli.

## Servisler

- **`DailyPercentagesAPI.kt`**: Günlük yüzdelik değişimleri çekmek için REST API servisi.
- **`GoldPricesAPI.kt`**: Güncel altın fiyatlarını çekmek için REST API servisi.

## Adaptörler

- **`RecyclerViewAdapter.kt`**: Altın piyasa verilerini listelemek için RecyclerView'a veri bağlamayı yöneten adaptör.

## Layoutlar

- **`activity_main.xml`**: Uygulamanın ana ekran düzenini tanımlar.
- **`activity_register.xml`**: Kullanıcı kayıt ekranı için layout.
- **`activity_login.xml`**: Kullanıcı giriş ekranı için layout.
- **`header.xml`** ve **`footer_navigation.xml`**: Farklı ekranlar arasında tutarlı bir UI için yeniden kullanılabilir layoutlar.
- **`row_layout.xml`**: Piyasa listesindeki bireysel öğelerin düzenini tanımlar.

![login](https://github.com/mulosbron/komutlar/assets/91866065/7037b31f-ec8f-4070-82e2-803c6732c59b)
![register](https://github.com/mulosbron/komutlar/assets/91866065/0078986c-4edd-454f-af30-70aa9f036b6d)
![markets](https://github.com/mulosbron/komutlar/assets/91866065/00929fb9-f402-4912-8045-6658ee5c8fb4)
>>>>>>> 92616cf (Üye olma ve giriş yapma ekranları eklendi.)
