# GoldMarketCap

GoldMarketCap, Kotlin dili kullanılarak geliştirilen bir Android uygulamasıdır ve gerçek zamanlı altın fiyat takibi ve piyasa verilerini sağlamak için tasarlanmıştır.

## Temel Bileşenler

- **`MainActivity.kt`**: Uygulamanın ana ekranını yönetir ve altın piyasaları fiyatlarını gösterir.
- **`RegisterActivity.kt`**: Yeni kullanıcıların üyelik işlemlerini yönetir.
- **`LoginActivity.kt`**: Kullanıcıların hesaplarına giriş yapmalarını sağlar.
- **`FooterActivity.kt`**: Navigasyonu kontrol eder ve uygulamanın farklı bölümlerinden erişilebilen ortak özellikleri gösterir.
- **`ForgotPasswordActivity.kt ve ResetPasswordActivity.kt`**: Kullanıcıların hesaplarının şifrelerini değiştirmelerini sağlar. 
- **`EmptyPorfolioActivity.kt`**: Boş portföy sayfası. Kullanıcıların işlem eklemesini sağlar.
- **`AddTransactionActivity.kt ve AddItemActivity.kt`**: Kullanıcıların seçtiği bir altın türünü portföylerine eklemelerini sağlar.

## Modeller

- **`DailyPercentage.kt`**: Altın fiyatlarındaki günlük yüzdelik değişimleri temsil eden veri modeli.
- **`GoldPrice.kt`**: Altın fiyatlarını temsil eden veri modeli.
- **`UserAuth.kt`**: Kullanıcı veri modeli.
- **`Transaction.kt`**: Porföye işlem eklemek için gerekli model.

## Servisler

- **`DailyPercentagesAPI.kt`**: Günlük yüzdelik değişimleri çekmek için REST API servisi.
- **`GoldPricesAPI.kt`**: Güncel altın fiyatlarını çekmek için REST API servisi.
- **`UserAPI.kt`**: Kullanıcı için REST API servisi.
- **`QueryAPI.kt`**: Arama çubuğu için REST API servisi.
- **`PortfolioAPI.kt`**: Portföy kısmı için REST API servisi.

## Adaptörler

- **`RecyclerViewAdapter.kt`**: Altın piyasa verilerini listelemek için RecyclerView'a veri bağlamayı yöneten adaptör.

## Layoutlar

- **`activity_main.xml`**: Uygulamanın ana ekran düzenini tanımlar.
![markets](https://github.com/mulosbron/GoldMarketCap/assets/91866065/3d6767c3-2131-4fe6-9099-abe360ce3547)


- **`activity_register.xml`**: Kullanıcı kayıt ekranı için layout.
![register](https://github.com/mulosbron/GoldMarketCap/assets/91866065/fa0a5f32-e6b0-4d9c-a8d5-d3d538e8ddf9)


- **`activity_login.xml`**: Kullanıcı giriş ekranı için layout.
![login](https://github.com/mulosbron/GoldMarketCap/assets/91866065/f02815c8-88cf-4449-bd8d-56f06c1be785)


- **`activity_forgot_password.xml ve activity_reset_password.xml`**: Kullanıcının şifresini unutması durumunda kullanılacak layoutlar. 
![forgot](https://t56kj3z524lm3ht7rwp6unwuw7osdsy2w4f2x7z6twoq246uie6a.arweave.net/n3yk7z3XFs2ef42f6jbUt90hyxq3C6v_Pp2dDXPUQTw)

![forgot](https://l6dpgwda7t4yevzwawl6tjbmtb2afcutc5kw4acib2j7tnbnmixa.arweave.net/X4bzWGD8-YJXNgWX6aQsmHQCipMXVW4ASA6T-bQtYi4)


- **`activity_empty_portfolio.xml`**: Kullanıcının portföyü boş ise karşısına çıkacak layout.
![empty_porfolio](https://6mpv22uurkqob6zwkxrjkfrm2docyidcxtnaireqesn3jasvn55q.arweave.net/8x9dapSKoOD7NlXilRYs0NwsIGK82gREkCSbtIJVb3s)


- **`activity_add_transaction.xml`** ve **`activity_add_item.xml`**: Kullanıcının portföyüne işlem eklemesi için kullanılan layoutlar.
![transaction](https://2gejynmvp4h3kc7ufswit35pcki5ax72nzid3gyt7qtmqn5necsa.arweave.net/0YicNZV_D7UL9Cysie-vEpHQX_puUD2bE_wmyDetIKQ)

![item](https://w6n7g5ezdkl2a6woqp5y5iiqpxuvjpi5qco4oevejchlxc3gaf6q.arweave.net/t5vzdJkal6B6zoP7jqEQfelUvR2AnccSpEiOu4tmAX0)


- **`header.xml`** ve **`footer_navigation.xml`**: Farklı ekranlar arasında tutarlı bir UI için yeniden kullanılabilir layoutlar.


- **`row_layout.xml`**: Piyasa listesindeki bireysel öğelerin düzenini tanımlar.
