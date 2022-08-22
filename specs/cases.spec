# Hepsiburada API Test Süreci

## Grocery api pozitif test senaryoları

* Tüm ürünlerin get servisi ile çekilip response kontrol edilmesi
* İsme göre bir ürünün get ile çekilip dataların kontrol edilmesi
* Yeni bir ürünün başarılı bir şeklilde eklenmesi
* Yeni eklenen ürünün get servisi üzerinden datalarının kontrol edilmesi
* Yeni eklenen ürünün put servisi ile datalarının güncellenmesi
* Yeni ürünün delete servisi silinmesi ve get servisi ile kontrolü

## Grocery api negatif test senaryoları

* Post servisi ile 500 hata kodunun simüle edilmesi
* Post servisi ile 400 hata kodunun simüle edilmesi
* Get servisi ile 404 hata kodunun simüle edilmesi