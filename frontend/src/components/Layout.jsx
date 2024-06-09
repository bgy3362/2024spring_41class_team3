// Layout.jsx
import React from 'react';
import Header from './Header';
import Footer from './Footer';

const Layout = ({ children }) => (
	<div>
		<Header />
		<div className="min-h-dvh w-dvw px-16 py-8">{children}</div>
		<Footer />
	</div>
);

export default Layout;
