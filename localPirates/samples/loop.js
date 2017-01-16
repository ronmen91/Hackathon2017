const readline = require('readline');
const rl = readline.createInterface({
	input: process.stdin,
	output: process.stdout,
	prompt: ''
});

module.exports = function (logic) {
	rl.on('line', (line) => {

		var message = JSON.parse(line);
		var fn = logic[message.event];

		if (fn) {
			var response = fn(message.data, rl);
			if (typeof (response) !== "undefined")
				console.log(JSON.stringify(response));
		}

		rl.prompt();
	}).on('close', () => {
		process.exit(0);
	});

	// kickoff the process
	rl.prompt();
};