# Coffee Bliss — Laporan Perbaikan & Cara Run

## Kenapa "corrupt terus kalau run"

Masalahnya bukan di kode Kotlin (kode-nya sehat), tapi di **struktur/konfigurasi project**:

1. **Tidak ada Gradle wrapper.** Folder `gradle/wrapper/` dan file `gradlew` hilang, jadi Android Studio tidak tahu versi Gradle mana yang dipakai.
2. **Versi Gradle bentrok.** Cache lama memakai **Gradle 9.0.0**, padahal **Android Gradle Plugin (AGP) 8.2.0 hanya mendukung Gradle 8.2–8.x**. Kombinasi AGP 8.2 + Gradle 9 = sync gagal / "corrupt".
3. **Launcher icon hilang.** `AndroidManifest.xml` memanggil `@mipmap/ic_launcher` dan `@mipmap/ic_launcher_round`, tapi tidak ada satu pun file mipmap → build gagal `resource mipmap/ic_launcher not found`.
4. **Folder resource korup.** Ada folder dengan nama harfiah `{drawable,values,xml}` (akibat brace expansion gagal di Windows cmd). Nama folder ini ilegal untuk aapt2 dan bisa menggagalkan build.

## Yang sudah diperbaiki (otomatis)

- ✅ Menambahkan `gradle/wrapper/gradle-wrapper.properties` → **Gradle 8.2** (kompatibel dgn AGP 8.2.0)
- ✅ Menambahkan `gradlew` + `gradlew.bat`
- ✅ Menghapus cache `.gradle` lama (Gradle 9) dan `build/` stale
- ✅ Menghapus folder korup `{drawable,values,xml}`
- ✅ Membuat adaptive launcher icon (cangkir kopi): `ic_launcher` & `ic_launcher_round` + `colors.xml` (minSdk 26, jadi cukup adaptive icon tanpa PNG)
- ✅ Menghapus method mati `addTransaction()` di repository yang berisi pemanggilan janggal `getMemberByEmail("")`

## Cara run di Android Studio

1. **Tutup** project lalu **buka ulang** (atau **File → Sync Project with Gradle Files**).
2. Saat sync pertama, Android Studio akan otomatis mengunduh Gradle 8.2 (perlu internet).
3. Pilih emulator / device → tekan **Run ▶**.

> Catatan: `gradle-wrapper.jar` akan dibuat otomatis oleh Android Studio saat sync pertama. Kalau diminta, klik **"OK"/"Use Gradle wrapper"**.

## Verifikasi 5 fitur (cek terhadap kode)

| Fitur | Status | Bukti di kode |
|---|---|---|
| 1. Registrasi Member (Nama, Email, No HP → Room) | ✅ | `RegisterScreen` → `register()` → `Member(name,email,phone)` → `memberDao.insert` |
| 2. Digital Membership Card (Nama, No Member, Status, Point) | ✅ | `MemberCardScreen` menampilkan `name`, `memberNumber` (CB0001...), `status`, `totalPoints` + QR |
| 3. Riwayat Transaksi (Tanggal, Nominal, Point) | ✅ | `Transaction(amount, pointsEarned, transactionDate)` → `HistoryScreen` |
| 4. Reward Point (Rp10.000 = 1 Point) | ✅ | `points = (amount / 10000).toInt()` → Rp150.000 = **15 point** (sesuai contoh) |
| 5. Redeem Point | ✅ | `RewardCatalog`: Espresso **50**, Cappuccino **100**, Latte Gratis **150** (persis sesuai spesifikasi) |

Semua fitur sudah terimplementasi sesuai spesifikasi. Logika poin dan katalog redeem **tepat** sama dengan permintaan.

## Catatan soal "menjalankan"

Build & emulator Android berjalan di mesin Windows kamu (butuh Android SDK + emulator), bukan di lingkungan ini — tidak ada MCP server yang menggantikan itu. Yang bisa diotomasi penuh dari sini adalah perbaikan project + analisis kode (sudah dilakukan). Untuk benar-benar menekan Run, itu di Android Studio kamu.
