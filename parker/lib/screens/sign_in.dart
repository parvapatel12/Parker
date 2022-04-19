import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:parker/components/custom_text_field.dart';
import 'package:parker/components/custom_button.dart';
import 'sign_up.dart';
import 'dashboard.dart';
import 'admin_home.dart';
import 'package:firebase_auth/firebase_auth.dart';

class SignIn extends StatefulWidget {
  const SignIn({Key? key}) : super(key: key);
  static String id = 'SignIn';

  @override
  State<SignIn> createState() => _SignInState();
}

class _SignInState extends State<SignIn> {
  final _auth = FirebaseAuth.instance;
  final _formKey = GlobalKey<FormState>();

  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  bool isObscure = true;

  @override
  Widget build(BuildContext context) {
    //double w = MediaQuery.of(context).size.width;
    double h = MediaQuery.of(context).size.height;
    return SafeArea(
      child: Scaffold(
        body: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10.0),
            child: Form(
              key: _formKey,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  SizedBox(
                    height: h * 0.1,
                  ),
                  const Text(
                    'Hello Again!',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: Colors.black54,
                      fontSize: 35.0,
                      fontFamily: "Horizon",
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  SizedBox(
                    height: h * 0.01,
                  ),
                  const Text(
                    'Welcome back you\'ve\nbeen missed!',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: Colors.black54,
                      fontSize: 20.0,
                      fontFamily: "Horizon",
                    ),
                  ),
                  SizedBox(
                    height: h * 0.05,
                  ),
                  CustomTextField(
                    hintText: 'Enter email',
                    textFieldInput: TextInputType.emailAddress,
                    textController: emailController,
                    customValidator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter Email';
                      }
                      return null;
                    },
                  ),
                  SizedBox(
                    height: h * 0.02,
                  ),
                  CustomTextField(
                    obscureText: isObscure,
                    customValidator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter password';
                      }
                      if (value.toString().length <= 6) {
                        return 'Password is too week';
                      }
                      return null;
                    },
                    iconButton: IconButton(
                      padding: const EdgeInsets.only(right: 15.0),
                      focusColor: Colors.black38,
                      onPressed: () {
                        setState(() {
                          isObscure = !isObscure;
                        });
                      },
                      icon: Icon(
                          isObscure ? Icons.visibility : Icons.visibility_off),
                    ),
                    hintText: 'Password',
                    textFieldInput: TextInputType.visiblePassword,
                    textController: passwordController,
                  ),
                  SizedBox(
                    height: h * 0.1,
                  ),
                  CustomButton(
                    buttonHeight: h * 0.06,
                    title: 'Sign In',
                    textSize: 17.0,
                    buttonFunction: () async {
                      try {
                        if (_formKey.currentState!.validate()) {
                          await _auth.signInWithEmailAndPassword(
                              email: emailController.text.trim(),
                              password: passwordController.text);
                          if (emailController.text.trim() == 'admin@nexus.co' &&
                              passwordController.text == 'admin123') {
                            Navigator.pushNamedAndRemoveUntil(
                                context, AdminHome.id, (route) => false);
                          } else {
                            Navigator.pushNamedAndRemoveUntil(
                                context, DashBoard.id, (route) => false);
                          }
                        }
                      } catch (e) {
                        if (kDebugMode) {
                          print(e);
                        }
                      }
                    },
                  ),
                  SizedBox(
                    height: h * 0.01,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text(
                        'Not a member?',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: Colors.black54,
                          fontSize: 15.0,
                          fontFamily: "Horizon",
                        ),
                      ),
                      TextButton(
                        onPressed: () {
                          Navigator.popAndPushNamed(context, SignUp.id);
                        },
                        child: const Text('Register Now'),
                        style: ButtonStyle(
                          padding: MaterialStateProperty.all(
                              const EdgeInsets.symmetric(horizontal: 3.0)),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
