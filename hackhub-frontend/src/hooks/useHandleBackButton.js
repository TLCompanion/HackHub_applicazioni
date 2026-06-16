import { useEffect } from "react";

export default function useHandleBackButton(navigate) {
    useEffect(() => {
        let isProcessing = false;

        const handleBackButton = () => {
            if (isProcessing) return;
            
            isProcessing = true;
            
            const conferma = window.confirm(
                "Sei sicuro di voler effettuare il logout?"
            );

            if (conferma) {
                localStorage.removeItem("token");
                navigate("/", { replace: true });
            } else {
                isProcessing = false;
                window.history.pushState(null, "", window.location.pathname);
            }
        };

        window.history.pushState(null, "", window.location.pathname);
        window.addEventListener("popstate", handleBackButton);

        return () => {
            window.removeEventListener("popstate", handleBackButton);
        };
    }, [navigate]);
}
