# GoldMarketCap

GoldMarketCap, Kotlin dili kullanılarak geliştirilen bir Android uygulamasıdır ve gerçek zamanlı altın fiyat takibi ve piyasa verilerini sağlamak için tasarlanmıştır.

## Temel Bileşenler

- **`MainActivity.kt`**: Uygulamanın ana ekranını yönetir. bottomNavigationView'i barındırır.
- **`MarketFragment.kt`**: Güncel piyasa verilerini kullanıcıya gösterir.
- **`RegisterFragment.kt`**: Yeni kullanıcıların üyelik işlemlerini yönetir.
- **`LoginFragment.kt`**: Kullanıcıların hesaplarına giriş yapmalarını sağlar.
- **`SettingsFragment.kt`**: Kullanıcıların hesaplarından çıkış yapmalarını sağlar. İleri de başka özellikler içerecek.
- **`ForgotPasswordFragment.kt ve ResetPasswordFragment.kt`**: Kullanıcıların hesaplarının şifrelerini değiştirmelerini sağlar. 
- **`EmptyPorfolioFragment.kt`**: Boş portföy sayfası. Kullanıcıların işlem eklemesini sağlar.
- **`PorfolioFragment.kt`**: Kullanıcın porföyüne eklediği assetleri gösterir. Kullanıcıların işlem eklemesini sağlar.
- **`AddTransactionActivity.kt ve AddAssetActivity.kt`**: Kullanıcıların seçtiği bir altın türünü portföylerine eklemelerini sağlar.

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
- **`APIService.kt`**: Ekranlarda yaptığımız işlemler için gerekli API fonksiyonlarını barındıran servis. 

## Adaptörler

- **`RecyclerViewAdapter.kt`**: Altın piyasa verilerini listelemek için RecyclerView'a veri bağlamayı yöneten adaptör.
- **`PortfolioAdapter.kt`**: Kullanıcı portföy verilerini listelemek için RecyclerView'a veri bağlamayı yöneten adaptör.

## Layoutlar [2024/05/10]

- **`fragment_market.xml`**: Uygulamanın ana ekran düzenini tanımlar.
![market](https://uadspyr2z4tvguaplgwat62skjwb6b2w7h4nfh45ej7rncwbcpla.arweave.net/oAcn4jrPJ1NQD1msCftSUmwfB1b5-NKfnSJ_ForBE9Y)


- **`fragment_register.xml`**: Kullanıcı kayıt ekranı için layout.
![register](https://jf4tj2enadeltlicciuwzynzwef33abhvx4k4emyekf64cqfvrza.arweave.net/SXk06I0AyLmtAhIpbOG5sQu9gCet-K4RmCKL7goFrHI)


- **`fragment_login.xml`**: Kullanıcı giriş ekranı için layout.
![login](https://o7vhspiro3ogcdgwm7xsmp4yzo6lh6f2pjmdyvwq4tsrrdpirira.arweave.net/d-p5PRF23GEM1mfvJj-Yy7yz-Lp6WDxW0OTlGI3oiiI)


- **`fragment_settings.xml`**: Kullanıcı giriş ekranı için layout.
![settings](https://rse56ah47ri2a3cpzxrpzlpcojntuknivtvqdpglyd2a7mgizoja.arweave.net/jInfAPz8UaBsT83i_K3icls6Kais6wG8y8D0D7DIy5I)


- **`fragment_forgot_password.xml ve fragment_reset_password.xml`**: Kullanıcının şifresini unutması durumunda kullanılacak layoutlar. 
![forgot](https://g4xo42gy2y2epmajlive3m6arcsdqb33nrawojovqdpcpfyenrra.arweave.net/Ny7uaNjWNEewCVoqTbPAiKQ4B3tsQWcl1YDeJ5cEbGI)

![reset](https://bzfbqg4qzlqt3kob4333omkhx7qz6t4chkywsof7bblq2liwdyxq.arweave.net/DkoYG5DK4T2pweb3tzFHv-GfT4I6sWk4vwhXDS0WHi8)


- **`activity_empty_portfolio.xml`**: Kullanıcının portföyü boş ise karşısına çıkacak layout.
![empty_porfolio](https://6mpv22uurkqob6zwkxrjkfrm2docyidcxtnaireqesn3jasvn55q.arweave.net/8x9dapSKoOD7NlXilRYs0NwsIGK82gREkCSbtIJVb3s)


- **`fragment_portfolio.xml`**: Kullanıcının portföyü boş ise karşısına çıkacak layout.
![porfolio](https://g4nzham33sw2v2kpzddj3zb3k5sw2sffcyiadsr34tj3h5vtktlq.arweave.net/NxuTgZvcrarpT8jGneQ7V2VtSKUWEAHKO-TTs_azVNc)


- **`fragment_add_transaction.xml`** ve **`fragment_add_asset.xml`**: Kullanıcının portföyüne işlem eklemesi için kullanılan layoutlar.
![transaction](https://2sc32wfxfjyh6ysblg32675uuuhqv5vnh6fawvgacxfa3kbzl7jq.arweave.net/1IW9WLcqcH9iQVm3r3-0pQ8K9q0_igtUwBXKDag5X9M)

![asset](https://emieg4juzc5lwhddkw6iv5mj266yuedl5spsumgfmo7o6hylwcea.arweave.net/IxBDcTTIurscY1W8ivWJ172KEGvsnyowxWO-7x8LsIg)


- **`header.xml`**: Farklı ekranlar arasında yeniden kullanılabilir olan layout.

- **`row_layout.xml`**: Piyasa listesindeki bireysel öğelerin düzenini tanımlar.

- **`row_layout_portfolio.xml`**: Portföy listesindeki bireysel öğelerin düzenini tanımlar.

-----------------------------------------------------------------------------------------

## Layoutlar [2024/05/03]

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
