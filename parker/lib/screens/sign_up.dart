// ignore_for_file: avoid_print

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:parker/components/custom_button.dart';
import 'package:parker/components/custom_text_field.dart';
import 'sign_in.dart';
import 'dashboard.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

class SignUp extends StatefulWidget {
  const SignUp({Key? key}) : super(key: key);
  static String id = 'SignUp';

  @override
  State<SignUp> createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final CollectionReference _users =
      FirebaseFirestore.instance.collection('users');
  final _formKey = GlobalKey<FormState>();

  TextEditingController fNameController = TextEditingController();
  TextEditingController lNameController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController passwordConfirmController = TextEditingController();
  bool isObscureConfirm = true;
  bool isObscure = true;

  Future<void> addUser(CollectionReference users) async {
    if (_auth.currentUser?.uid != null) {
      return await users
          .doc(_auth.currentUser?.uid)
          .set({
            'fName': fNameController.text,
            'lName': lNameController.text,
            'email': emailController.text,
            'password': passwordController.text,
          })
          .then((value) => print("User Added"))
          .catchError((error) => print("Failed to add user: $error"));
    }
  }

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
                //mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  SizedBox(
                    height: h * 0.1,
                  ),
                  const Text(
                    'Hello!',
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
                    'Signup to get started',
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
                  Row(
                    children: [
                      Expanded(
                        child: CustomTextField(
                          hintText: 'FName',
                          rightPadding: 5.0,
                          textFieldInput: TextInputType.name,
                          textController: fNameController,
                          customValidator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Enter First Name';
                            }
                            return null;
                          },
                        ),
                      ),
                      Expanded(
                        child: CustomTextField(
                          hintText: 'LName',
                          leftPadding: 5.0,
                          textFieldInput: TextInputType.name,
                          textController: lNameController,
                          customValidator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Enter Last Name';
                            }
                            return null;
                          },
                        ),
                      ),
                    ],
                  ),
                  SizedBox(
                    height: h * 0.02,
                  ),
                  CustomTextField(
                    hintText: 'Enter email',
                    textFieldInput: TextInputType.emailAddress,
                    textController: emailController,
                    customValidator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Enter Email Address';
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
                        return 'Password is to week';
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
                    height: h * 0.02,
                  ),
                  CustomTextField(
                    obscureText: isObscureConfirm,
                    customValidator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter password';
                      }
                      if (value.toString().length <= 6) {
                        return 'Password is to week';
                      }
                      if (value.toString() != passwordController.text) {
                        return 'Password do not match';
                      }

                      return null;
                    },
                    iconButton: IconButton(
                      padding: const EdgeInsets.only(right: 15.0),
                      focusColor: Colors.black38,
                      onPressed: () {
                        setState(() {
                          isObscureConfirm = !isObscureConfirm;
                        });
                      },
                      icon: Icon(
                          isObscure ? Icons.visibility : Icons.visibility_off),
                    ),
                    hintText: 'Confirm Password',
                    textFieldInput: TextInputType.visiblePassword,
                    textController: passwordConfirmController,
                  ),
                  SizedBox(
                    height: h * 0.04,
                  ),
                  CustomButton(
                    buttonHeight: h * 0.06,
                    title: 'Sign Up',
                    textSize: 17.0,
                    buttonFunction: () async {
                      try {
                        if (_formKey.currentState!.validate()) {
                          await _auth
                              .createUserWithEmailAndPassword(
                                  email: emailController.text,
                                  password: passwordController.text)
                              .whenComplete(
                                () => addUser(_users),
                              )
                              .whenComplete(
                                () => Navigator.popAndPushNamed(
                                    context, DashBoard.id),
                              );
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
                        'Already have an Account?',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: Colors.black54,
                          fontSize: 15.0,
                          fontFamily: "Horizon",
                        ),
                      ),
                      TextButton(
                        onPressed: () {
                          Navigator.popAndPushNamed(context, SignIn.id);
                        },
                        child: const Text('Sign In    '),
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
