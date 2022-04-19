import 'package:flutter/material.dart';

class CustomButton extends StatelessWidget {
  const CustomButton({
    Key? key,
    this.buttonHeight,
    this.buttonWidth,
    this.title,
    this.textSize,
    this.buttonFunction,
  }) : super(key: key);
  final double? buttonWidth;
  final double? buttonHeight;
  final String? title;
  final double? textSize;
  final VoidCallback? buttonFunction;

  @override
  Widget build(BuildContext context) {
    // double w = MediaQuery.of(context).size.width;
    double h = MediaQuery.of(context).size.height;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: RawMaterialButton(
        onPressed: buttonFunction,
        child: Container(
          height: (buttonHeight == null) ? h * 0.05 : buttonHeight,
          width: buttonWidth,
          decoration: BoxDecoration(
            color: const Color(0xFFFF7560),
            border: Border.all(
              color: const Color(0xFFFF7560),
              width: 1,
            ),
            borderRadius: const BorderRadius.all(
              Radius.circular(15.0),
            ),
          ),
          child: Center(
            child: Text(
              '$title',
              style: TextStyle(
                color: Colors.white70,
                fontSize: textSize,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
        ),
      ),
    );
  }
}
