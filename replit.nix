{pkgs}: {
  deps = [
    pkgs.nsis
    pkgs.wineWowPackages.stable
    pkgs.wine64
    pkgs.gradle
    pkgs.temurin-bin-17
  ];
}
