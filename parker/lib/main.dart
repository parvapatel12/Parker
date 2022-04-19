import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'screens/intro.dart';
import 'screens/sign_in.dart';
import 'screens/sign_up.dart';
import 'screens/dashboard.dart';
import 'screens/admin_home.dart';
import 'screens/qr_scanner.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  // await Firebase.initializeApp();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);
  // final _navigatorKey = GlobalKey<NavigatorState>();

  @override
  Widget build(BuildContext context) {
    SystemChrome.setPreferredOrientations(
      [DeviceOrientation.portraitUp],
    );
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Career Mentor',
      theme: ThemeData.light().copyWith(
        scaffoldBackgroundColor: const Color(0xFFD0D4E3),
        appBarTheme: const AppBarTheme(
          color: Colors.purpleAccent,
        ),
      ),
      home: FutureBuilder(
          future: Firebase.initializeApp(),
          builder: (_, snapshot) {
            if (snapshot.hasError) return const Intro();
            if (snapshot.connectionState == ConnectionState.done) {
              // Assign listener after the SDK is initialized successfully
              FirebaseAuth.instance.authStateChanges().listen((User? user) {
                if (user == null) {
                  Navigator.pushReplacementNamed(context, Intro.id);
                } else {
                  Navigator.pushReplacementNamed(context, DashBoard.id);
                }
              });
            }

            return const Center(child: CircularProgressIndicator());
          }),
      initialRoute: Intro.id,
      routes: {
        Intro.id: (context) => const Intro(),
        SignIn.id: (context) => const SignIn(),
        SignUp.id: (context) => const SignUp(),
        DashBoard.id: (context) => const DashBoard(),
        AdminHome.id: (context) => const AdminHome(),
        QrScanner.id: (context) => const QrScanner(),
      },
    );
  }
}
