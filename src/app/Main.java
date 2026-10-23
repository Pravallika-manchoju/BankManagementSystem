package app;

import controller.BankController;
import repository.BankRepository;
import service.BankService;
import storage.CollectionBankRepository;
import view.consoleView;

public class Main {
    public static void main(String[] args) {
        consoleView view = new consoleView();
        while (true) {
            int choice = view.showStorageMenu();
            BankRepository repository;
            switch (choice) {
                case 1:
                    repository = new CollectionBankRepository();
                    view.showMessage("Collections storage selected.");
                    break;
                case 2:
                    view.showMessage("File storage is not implemented yet.");
                    continue;
                case 3:
                    view.showMessage( "PostgreSQL storage is not implemented yet.");
                    continue;
                case 0:
                    view.showMessage("Application closed.");
                    return;
                default:
                    view.showMessage("Invalid selection. Try again.");
                    continue;
            }
            BankService service = new BankService(repository);
            BankController controller = new BankController(service, view);
            controller.start();
        }
    }
}
