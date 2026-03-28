import UIKit
import SwiftUI
import ComposeApp

struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        mainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
