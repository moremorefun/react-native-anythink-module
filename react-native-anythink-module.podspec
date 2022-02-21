require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-anythink-module"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://github.com/moremorefun/react-native-anythink-module.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm}"

  s.dependency "React-Core"
  s.dependency 'AnyThinkiOS','5.8.12'
  s.dependency 'AnyThinkiOS/AnyThinkTTAdapter','5.8.12'
  s.dependency 'AnyThinkiOS/AnyThinkGDTAdapter','5.8.12'
  s.dependency 'AnyThinkiOS/AnyThinkBaiduAdapter','5.8.12'
end
