// swift-tools-version: 5.9
import PackageDescription

let package = Package(
  name: "exportedAppsFlyerSDK",
  platforms: [.iOS("12.0"), .macOS("10.13"), .tvOS("12.0"), .watchOS("4.0")],
  products: [
    .library(
      name: "exportedAppsFlyerSDK",
      type: .static,
      targets: ["exportedAppsFlyerSDK", "AppsFlyerLib"])
  ],
  dependencies: [],
  targets: [
    .target(
      name: "exportedAppsFlyerSDK",
      dependencies: [
        "AppsFlyerLib"
      ],
      path: "Sources"

    ),
    .binaryTarget(
      name: "AppsFlyerLib",
      url:
        "https://github.com/AppsFlyerSDK/AppsFlyerFramework/releases/download/6.16.2/AppsFlyerLib-Dynamic-SPM.xcframework.zip",
      checksum: "6ce9bf6da08f85f6eafac2520ef0d0579d0724b3b2200cb46dcc18993cd02608"),
  ]
)
