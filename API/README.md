# API Projesi

Veri işleme için Python scriptleri ve backend için .NET Web API'sini entegre ederek, veri yönetimi ve sunumu için kapsamlı bir çözüm oluşturur. Python scriptleri, veri toplama ve işlemeden sorumludur ve sonuçları MongoDB'de saklar. .NET Web API ise backend olarak görev yapar ve işlenmiş verileri müşterilere sunar.

## Proje Yapısı

Repo, projenin farklı bileşenlerini ayırmak için birkaç dizine ayrılmıştır:

- `python_scripts/`: Veri toplama ve işleme için Python scriptlerini içerir.
    - `gold_price_daily_percentage.py`: Altın fiyat verilerini işleyen script.
    - `milliyet_altin.py`: Milliyet'ten altın verilerini çeken ve işleyen script.
- `systemd/`: Python scriptlerini planlama ve çalıştırma için systemd service ve timer dosyalarını içerir.
    - `services/`: Scriptlerin servis olarak yönetilmesi için service dosyaları.
    - `timers/`: Serviceleri planlamak için timer dosyaları.
- `dotnet/`: Backend için .NET Web API projesini içerir.

## Kurulum

### MongoDB

Sisteminizde MongoDB'nin kurulu ve doğru şekilde yapılandırıldığından emin olun. Kurulum talimatları için MongoDB dokümantasyonuna başvurun.

### Python Scriptleri

Python 3.7 veya daha yeni bir sürüm gereklidir. Python scriptleri için gerekli kütüphaneler pip kullanılarak kurulabilir:

```bash
pip install -r requirements.txt
```

## Kullanım

### Script Klasörünün Taşınması

`python_scripts` dizini içerisinde yer alan `.py` adlı klasör, içerisindeki tüm script dosyaları ile birlikte `/root/` kök dizinine taşınmalıdır. Bu işlem için aşağıdaki komut kullanılabilir:

```bash
sudo cp -r python_scripts/.py /root/
```
### Scriptleri Planlama

`systemd` dizini, Python scriptlerini planlamak için service ve timer birimlerini içerir. Bu birimleri `/etc/systemd/system/` dizinine kopyalayarak ve timer etkinleştirerek kurun:
  
```bash
sudo cp systemd/services/* /etc/systemd/system/

sudo cp systemd/timers/* /etc/systemd/system/

sudo systemctl enable --now gold_price_daily_percentage.timer

sudo systemctl enable --now milliyet_altin.timer
```

