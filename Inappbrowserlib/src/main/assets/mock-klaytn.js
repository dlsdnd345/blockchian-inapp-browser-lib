(() => {
  window.klaytn = klaytn = {
    autoRefreshOnNetworkChange: false,
    endpointURL: "https://public-en.klutchwallet.com/v1/baobab",
    initialized: true,
    isKaikas: true,
    isKlutch: true,
    isMobile: true,
    networkVersion: null,
    selectedAddress: null,
    on (eventName, callback) {
      console.log("On", eventName, callback);
    },
    off (eventName, callback) {
      console.log("Off", eventName, callback);
    },
    removeListener (e, t) {
      console.log("removeListener", e, t);
    },
    isConnected () {
      return true;
    },
    sendAsync: async function (payload, callback) {
        const str = JSON.stringify(payload);
        const receipt = await window.appBridge.sendAsync(str);
        callback(null, {
            id: payload.id,
            jsonrpc: payload.jsonrpc,
            result: receipt
        })
    },
    enable: async function () {
      const address = await window.appBridge.enable();
      const network = await window.appBridge.getNetwork();
      console.log("enable return: ", address)
      if (!address) {
        return [];
      }

      klaytn.selectedAddress = address;
      klaytn.networkVersion = network;

      return [address]
    }
  };
})();