import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:parker/components/custom_button.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:parker/components/custom_text_field.dart';
import 'package:parker/services/api_call.dart';
import 'package:parker/screens/intro.dart';
import 'qr_scanner.dart';

class AdminHome extends StatefulWidget {
  const AdminHome({Key? key}) : super(key: key);
  static String id = 'AdminHome';

  @override
  State<AdminHome> createState() => _AdminHomeState();
}

class _AdminHomeState extends State<AdminHome> {
  final FirebaseAuth _auth = FirebaseAuth.instance;

  TextEditingController uidController = TextEditingController();

  Future sendApiResponse(String uID) async {
    var apiData = await ApiCall().sendApiData(uID);
    if (kDebugMode) {
      print(apiData);
    }
  }

  // Future getApiResponse() async {
  //   var apiData = await ApiCall().getApiData('${_auth.currentUser?.uid}');
  //   if (kDebugMode) {
  //     print(apiData);
  //   }
  // }

  Future<void> printUID() async {
    if (kDebugMode) {
      print(_auth.currentUser?.uid);
    }
  }

  @override
  Widget build(BuildContext context) {
    double w = MediaQuery.of(context).size.width;
    double h = MediaQuery.of(context).size.height;
    return SafeArea(
      child: Scaffold(
        resizeToAvoidBottomInset: false,
        body: Stack(
          fit: StackFit.expand,
          children: [
            Container(
              height: h * 0.5,
              decoration: const BoxDecoration(
                  // gradient: LinearGradient(
                  //     begin: Alignment.topLeft,
                  //     end: Alignment.bottomRight,
                  //     colors: [Colors.deepPurple, Colors.purpleAccent]),
                  ),
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    SizedBox(
                      height: h * 0.02,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          '',
                          style: TextStyle(
                            color: Colors.white70,
                            fontSize: 18.0,
                            fontFamily: "Horizon",
                          ),
                        ),
                        PopupMenuButton(
                            icon: const Icon(
                              Icons.menu,
                              color: Colors.black54,
                            ),
                            onSelected: (index) async {
                              if (index == 3) {
                                await _auth.signOut().whenComplete(() =>
                                    Navigator.pushNamedAndRemoveUntil(
                                        context, Intro.id, (route) => false));
                              }
                            },
                            itemBuilder: (context) => <PopupMenuEntry>[
                                  // const PopupMenuItem(
                                  //   value: 0,
                                  //   child: Text('History'),
                                  // ),
                                  const PopupMenuItem(
                                    value: 1,
                                    child: Text('Account'),
                                  ),
                                  const PopupMenuItem(
                                    value: 2,
                                    child: Text('Setting'),
                                  ),
                                  const PopupMenuItem(
                                    value: 3,
                                    child: Text('Log out'),
                                  ),
                                ]),
                      ],
                    ),
                    SizedBox(
                      height: h * 0.01,
                    ),
                    const Text(
                      "Hello, Admin.",
                      textAlign: TextAlign.left,
                      style: TextStyle(
                        color: Colors.black54,
                        fontSize: 30.0,
                        fontFamily: "Horizon",
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Positioned(
              width: w * 0.8,
              height: h * 0.1,
              bottom: h * 0.35,
              left: w * 0.1,
              child: CustomTextField(
                hintText: 'Enter uid',
                textFieldInput: TextInputType.text,
                textController: uidController,
              ),
            ),
            Positioned(
              width: w * 0.8,
              height: h * 0.05,
              bottom: h * 0.12,
              left: w * 0.1,
              child: CustomButton(
                title: 'API Call',
                textSize: 15.0,
                buttonFunction: () async {
                  await sendApiResponse(uidController.text.trim());
                },
              ),
            ),
            Positioned(
              width: w * 0.8,
              height: h * 0.05,
              bottom: h * 0.05,
              left: w * 0.1,
              child: CustomButton(
                title: 'Scan QRCode',
                textSize: 15.0,
                buttonFunction: () {
                  printUID();
                  Navigator.pushNamed(context, QrScanner.id);
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
