import UIKit
import SwiftUI
import ComposeApp

struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.mainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
