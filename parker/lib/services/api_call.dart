import 'package:parker/services/networking.dart';

const javaServerUrl = 'http://192.168.0.203:8080';
const urlPrefix = 'https://jsonplaceholder.typicode.com';

class ApiCall {
  Future<dynamic> getApiData(String uid) async {
    NetworkHelper networkHelper = NetworkHelper(
      '$javaServerUrl/checkentry?id=$uid',
    );

    var apiData = await networkHelper.getData();
    return apiData;
  }

  Future<dynamic> sendApiData(String uid) async {
    NetworkHelper networkHelper = NetworkHelper(
      '$javaServerUrl/enter?id=${uid.trim()}&validity=valid',
    );

    var apiData = await networkHelper.sendData();
    return apiData;
  }
}
