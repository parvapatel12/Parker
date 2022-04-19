import 'package:flutter/material.dart';
import 'package:mobile_scanner/mobile_scanner.dart';

MobileScannerController cameraController = MobileScannerController();

class QrScanner extends StatefulWidget {
  const QrScanner({Key? key}) : super(key: key);
  static String id = 'QrScanner';

  @override
  State<QrScanner> createState() => _QrScannerState();
}

class _QrScannerState extends State<QrScanner> {
  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Mobile Scanner'),
          backgroundColor: Colors.deepPurple,
          actions: [
            IconButton(
              color: Colors.white,
              icon: ValueListenableBuilder(
                valueListenable: cameraController.torchState,
                builder: (context, state, child) {
                  switch (state as TorchState) {
                    case TorchState.off:
                      return const Icon(Icons.flash_off, color: Colors.grey);
                    case TorchState.on:
                      return const Icon(Icons.flash_on, color: Colors.yellow);
                  }
                },
              ),
              iconSize: 32.0,
              onPressed: () => cameraController.toggleTorch(),
            ),
            IconButton(
              color: Colors.white,
              icon: ValueListenableBuilder(
                valueListenable: cameraController.cameraFacingState,
                builder: (context, state, child) {
                  switch (state as CameraFacing) {
                    case CameraFacing.front:
                      return const Icon(Icons.camera_front);
                    case CameraFacing.back:
                      return const Icon(Icons.camera_rear);
                  }
                },
              ),
              iconSize: 32.0,
              onPressed: () => cameraController.switchCamera(),
            ),
          ],
        ),
        body: MobileScanner(
          allowDuplicates: false,
          controller: cameraController,
          onDetect: (barcode, args) {
            final String? code = barcode.rawValue;
            debugPrint('Barcode found!; $code');
          },
        ),
      ),
    );
  }

  @override
  void dispose() {
    // TODO: implement dispose
    cameraController.dispose();
    super.dispose();
  }
}
