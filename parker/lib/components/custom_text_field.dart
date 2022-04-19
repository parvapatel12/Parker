import 'package:flutter/material.dart';

class CustomTextField extends StatelessWidget {
  const CustomTextField({
    Key? key,
    this.obscureText = false,
    this.rightPadding = 20.0,
    this.leftPadding = 20.0,
    this.iconButton,
    this.hintText,
    this.textFieldInput,
    this.customValidator,
    this.textController,
  }) : super(key: key);

  final TextInputType? textFieldInput;
  final TextEditingController? textController;
  final String? hintText;
  final bool obscureText;
  final IconButton? iconButton;
  final double rightPadding;
  final double leftPadding;
  final dynamic customValidator;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(right: rightPadding, left: leftPadding),
      child: TextFormField(
        obscureText: obscureText,
        keyboardType: textFieldInput,
        controller: textController,
        cursorColor: Colors.black54,
        validator: customValidator,
        decoration: InputDecoration(
          contentPadding:
              const EdgeInsets.symmetric(horizontal: 20.0, vertical: 15.0),
          suffixIcon: iconButton,
          hintText: hintText,
          hintStyle: const TextStyle(
            color: Colors.black26,
          ),
          filled: true,
          fillColor: Colors.white,
          focusColor: Colors.black12,
          border: const OutlineInputBorder(
            borderSide: BorderSide(
              color: Colors.white,
            ),
            borderRadius: BorderRadius.all(Radius.circular(15.0)),
          ),
          enabledBorder: const OutlineInputBorder(
            borderSide: BorderSide(
              color: Colors.white,
            ),
            borderRadius: BorderRadius.all(Radius.circular(15.0)),
          ),
          focusedBorder: const OutlineInputBorder(
            borderSide: BorderSide(
              color: Colors.white,
            ),
            borderRadius: BorderRadius.all(Radius.circular(15.0)),
          ),
        ),
      ),
    );
  }
}
