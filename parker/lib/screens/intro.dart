import 'package:flutter/material.dart';
import 'sign_in.dart';
import 'sign_up.dart';

class Intro extends StatefulWidget {
  const Intro({Key? key}) : super(key: key);
  static String id = 'Intro';

  @override
  State<Intro> createState() => _IntroState();
}

class _IntroState extends State<Intro> {
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
            Positioned(
              top: 0.0,
              left: -w * 0.4,
              child: SizedBox(
                height: h * 0.5,
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(20),
                  child: Image.asset('assets/images/intro_image.jpg'),
                ),
              ),
            ),
            Positioned(
              top: h * 0.45,
              left: 30.0,
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 10.0),
                child: Column(
                  children: <Widget>[
                    SizedBox(
                      height: h * 0.1,
                    ),
                    const Text(
                      "Pick Your\nPerfect Parking",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        color: Colors.black54,
                        fontSize: 35.0,
                        fontFamily: "Horizon",
                      ),
                    ),
                    SizedBox(
                      height: h * 0.08,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        RawMaterialButton(
                          onPressed: () {
                            Navigator.pushNamed(context, SignIn.id);
                          },
                          child: Container(
                            height: 60.0,
                            width: 150.0,
                            decoration: BoxDecoration(
                              color: Colors.white,
                              border: Border.all(
                                color: Colors.white,
                                width: 1,
                              ),
                              borderRadius: const BorderRadius.only(
                                //Radius.circular(15.0),
                                topLeft: Radius.circular(15.0),
                                topRight: Radius.zero,
                                bottomLeft: Radius.circular(15.0),
                                bottomRight: Radius.zero,
                              ),
                            ),
                            child: const Center(
                              child: Text(
                                'Sign In',
                                style: TextStyle(
                                  color: Colors.black54,
                                  fontSize: 18.0,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                        ),
                        RawMaterialButton(
                          onPressed: () {
                            Navigator.pushNamed(context, SignUp.id);
                          },
                          child: Container(
                            height: 60.0,
                            width: 150.0,
                            decoration: BoxDecoration(
                              border: Border.all(
                                color: Colors.white,
                                width: 1,
                              ),
                              borderRadius: const BorderRadius.only(
                                topLeft: Radius.zero,
                                topRight: Radius.circular(15.0),
                                bottomLeft: Radius.zero,
                                bottomRight: Radius.circular(15.0),
                              ),
                            ),
                            child: const Center(
                              child: Text(
                                'Sign Up',
                                style: TextStyle(
                                  color: Colors.black54,
                                  fontSize: 18.0,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ),
                        ),
                      ],
                    )
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
