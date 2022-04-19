import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class NetworkHelper {
  NetworkHelper(this.url);

  final String url;

  Future sendData() async {
    if (kDebugMode) {
      print(Uri.parse(url));
    }
    http.Response response = await http.post(
      Uri.parse(url),
    );

    if (response.statusCode == 201) {
      String data = response.body;
      return data;
    } else {
      if (kDebugMode) {
        print(response.statusCode);
      }
    }
  }

  Future getData() async {
    http.Response response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      String data = response.body;
      return data;
    } else {
      if (kDebugMode) {
        print(response.statusCode);
      }
    }
  }
}
