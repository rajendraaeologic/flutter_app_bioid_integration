import 'package:flutter/services.dart';

class BioId {

  static const MethodChannel _channel = MethodChannel('flutter.native/bioid');

  static Future<void> verify() async {
    return _channel.invokeMethod("verify");
  }

  static Future<void> enroll() async {
    return _channel.invokeMethod("enroll");
  }
}