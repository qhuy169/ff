import React, { createContext, useContext } from "react";
import { useState } from "react";
import LanguageUtils from "../utils/languageUtils";

const LanguageContext = createContext();

const LanguageProvider = ({ children }) => {
  const [language, setLanguage] = useState(
    LanguageUtils.getMessageByLang("vi")
  );
  
  const handleChangeLanguage = (lang) => {
    setLanguage(LanguageUtils.getMessageByLang(lang));
  };

  return (
    <LanguageContext.Provider
      value={{
        ...language,
        onChangeLanguage: handleChangeLanguage,
      }}
    >
      {children}
    </LanguageContext.Provider>
  );
};

const useLanguage = () => {
  const context = useContext(LanguageContext);

  if (context === undefined) {
    throw new Error("useLanguage must be used within LanguageProvider");
  }
  return context;
};

export { LanguageProvider, useLanguage };
